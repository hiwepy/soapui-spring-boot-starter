/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.smartbear.soapui.spring.boot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import com.eviware.soapui.model.iface.Operation;
import com.smartbear.soapui.spring.boot.utils.DOMUtil;
import com.smartbear.soapui.spring.boot.utils.OGNLUtils;
import com.smartbear.soapui.spring.boot.utils.XmlUtil;
import com.smartbear.soapui.spring.boot.wsdl.RomoteWsdlLoader;

public class SoapuiMessageTemplate {

	private static final Logger LOGGER = Logger.getLogger(SoapuiMessageTemplate.class);
	private DocumentBuilderFactory docBuilderFactory;

	public SoapuiMessageTemplate() {
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		this.docBuilderFactory.setNamespaceAware(true);
	}

	

	private String buildRequest(String wsdl, Operation operationInst, Map<String, Object> params,
			Properties httpClientProps, String smooksResource, String soapNs)
			throws IOException, UnsupportedOperationException, SAXException {
		String requestTemplate = operationInst.getRequestAt(0).getRequestContent();

		return buildSOAPMessage(requestTemplate, params, smooksResource, soapNs);
	}

	

	private String buildSOAPMessage(String soapMessageTemplate, Map<String, Object> params, String smooksResource,
			String soapNs) throws IOException, SAXException {
		Document messageDoc = getDocBuilder().parse(new InputSource(new StringReader(soapMessageTemplate)));

		Element docRoot = messageDoc.getDocumentElement();

		boolean dumpSOAP = params.containsKey("dumpSOAP");
		if (dumpSOAP) {
			dumpSOAP("SOAP Template (Unexpanded):", docRoot);
		}

		injectParameters(docRoot, params, soapNs);

		if (dumpSOAP) {
			dumpSOAP("SOAP Message (Populated Template):", docRoot);
		}

		return XmlUtil.serialize(messageDoc.getChildNodes());
	}

	private synchronized DocumentBuilder getDocBuilder() throws IOException {
		try {
			return this.docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			IOException ioe = new IOException("Could not create document builder");
			ioe.initCause(pce);
			throw ioe;
		}
	}

	private void dumpSOAP(String message, Element docRoot) {
		System.out.println(message + "\n");
		try {
			DOMUtil.serialize(docRoot, new StreamResult(System.out), true);
		} catch (ConfigurationException e) {
			LOGGER.error("Unable to dump SOAP.", e);
		}
	}

	private void injectParameters(Element element, Map<String, Object> params, String soapNs) {
		NodeList children = element.getChildNodes();
		int childCount = children.getLength();

		for (int i = 0; i < childCount; ++i) {
			Node node = children.item(i);

			if ((childCount == 1) && (node.getNodeType() == 3))
				if (node.getNodeValue().equals("?")) {
					String ognl = OGNLUtils.getOGNLExpression(element, soapNs);
					Object param = params.get(ognl);

					element.removeChild(node);
					element.appendChild(element.getOwnerDocument().createTextNode(param.toString()));
				} else if (node.getNodeType() == 1)
					injectParameters((Element) node, params, soapNs);
		}
	}

	private Map<String, String> populateResponseOgnlMap(String response) {
		Map map = new LinkedHashMap();
		try {
			DocumentBuilder docBuilder = getDocBuilder();
			Document doc = docBuilder.parse(new InputSource(new StringReader(response)));
			Element graphRootElement = getGraphRootElement(doc.getDocumentElement());

			populateResponseOgnlMap(map, graphRootElement);
		} catch (SAXException e) {
			throw new RuntimeException("Error parsing SOAP response.", e);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected error reading SOAP response.", e);
		}

		return map;
	}

	private void populateResponseOgnlMap(Map<String, String> map, Element element) {
		NodeList children = element.getChildNodes();
		int childCount = children.getLength();

		if (childCount == 1) {
			Node childNode = children.item(0);
			if (childNode.getNodeType() == 3) {
				String ognl = OGNLUtils.getOGNLExpression(element);
				map.put(ognl, childNode.getNodeValue());
				return;
			}

		}

		for (int i = 0; i < childCount; ++i) {
			Node childNode = children.item(i);
			if (childNode.getNodeType() == 1)
				populateResponseOgnlMap(map, (Element) childNode);
		}
	}

	private Element getGraphRootElement(Element element) {
		String ognl = OGNLUtils.getOGNLExpression(element);

		if ((ognl != null) && (!(ognl.equals("")))) {
			return element;
		}

		NodeList children = element.getChildNodes();
		int childCount = children.getLength();
		for (int i = 0; i < childCount; ++i) {
			Node node = children.item(i);
			if (node.getNodeType() == 1) {
				Element graphRootElement = getGraphRootElement((Element) node);
				if (graphRootElement != null) {
					return graphRootElement;
				}
			}
		}
		return null;
	}

	public static List<Map<String, String>> getOperationsSoapByWsdl(String wsdl) {
		List objlist = new ArrayList();
		try {
			WsdlProject wsdlProject = new WsdlProject();
			WsdlInterface[] wsdlInterfaces = wsdlProject.importWsdl(wsdl, true, createWsdlLoader(wsdl, null));
			for (WsdlInterface wsdlInterface : wsdlInterfaces) {
				List operationInsts = wsdlInterface.getOperations();
				for (Operation operationInst : operationInsts) {
					Map map = new HashMap();
					String requestTemplate = operationInst.getRequestAt(0).getRequestContent();
					map.put(operationInst.getName(), requestTemplate);
					objlist.add(map);
				}
			}
		} catch (Exception e) {
			LOGGER.error("获取方法名、soap数据异常", e);
			e.printStackTrace();
		}
		return objlist;
	}

	public static String getSoapByWsdlOperation(String wsdl, String operation) {
		List objlist = getOperationsSoapByWsdl(wsdl);
		if ((objlist != null) && (objlist.size() > 0)) {
			for (Map map : objlist) {
				if (map.containsKey(operation)) {
					return ((String) map.get(operation));
				}
			}
		}
		return null;
	}
	
}
