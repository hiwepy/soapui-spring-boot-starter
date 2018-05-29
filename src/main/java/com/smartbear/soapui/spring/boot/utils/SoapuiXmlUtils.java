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
package com.smartbear.soapui.spring.boot.utils;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlbeans.XmlObject;
import org.assertj.core.util.Lists;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.xml.XmlUtils;

public class SoapuiXmlUtils {
	
	private static String ELEMENT_NAME_FUNC = "/name()";
	private static XPathFactory xPathFactory = XPathFactory.newInstance();
	
	public static String getName(Node node) {
		String name = node.getLocalName();
		if (name != null) {
			return name;
		}
		return node.getNodeName();
	}
	
	public static String getName(Element element) {
		String name = element.getLocalName();
		if (name != null) {
			return name;
		}
		return element.getTagName();
	}
	
	public static NodeList getNodeList(Node node, String xpath) {
		if (node == null)
			throw new IllegalArgumentException("null 'document' arg in method call.");
		if (xpath == null)
			throw new IllegalArgumentException("null 'xpath' arg in method call.");
		try {
			XPath xpathEvaluater = xPathFactory.newXPath();

			if (xpath.endsWith(ELEMENT_NAME_FUNC)) {
				return ((NodeList) xpathEvaluater.evaluate(
						xpath.substring(0, xpath.length() - ELEMENT_NAME_FUNC.length()), node, XPathConstants.NODESET));
			}
			return ((NodeList) xpathEvaluater.evaluate(xpath, node, XPathConstants.NODESET));
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException("bad 'xpath' expression [" + xpath + "].");
		}
	}

	public static Node getNode(Node node, String xpath) {
		NodeList nodeList = getNodeList(node, xpath);

		if ((nodeList == null) || (nodeList.getLength() == 0)) {
			return null;
		}
		return nodeList.item(0);
	}
	
	public static int countElementsBefore(Node node, String tagName) {
		
		Node parent = node.getParentNode();

		NodeList siblings = parent.getChildNodes();
		int count = 0;
		int siblingCount = siblings.getLength();

		for (int i = 0; i < siblingCount; ++i) {
			Node sibling = siblings.item(i);

			if (sibling == node) {
				break;
			}
			if ((sibling.getNodeType() == 1) && (((Element) sibling).getTagName().equals(tagName))) {
				++count;
			}
		}

		return count;
	}
	
	public static Node getFirstChildByType(Element element, int nodeType) {
		NodeList children = element.getChildNodes();
		int childCount = children.getLength();

		for (int i = 0; i < childCount; ++i) {
			Node child = children.item(i);
			if (child.getNodeType() == nodeType) {
				return child;
			}
		}

		return null;
	}
	
	public static String extractRequest(String soapRequestBody, SoapVersion soapVersion, List<String> inputNames, List<String> inputTypes) throws SoapUIException {
		
		try {
			
			/*
			<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="namespace">
			   <soapenv:Header/>
			   <soapenv:Body>
			      <web:method1>
			         <web:param1>?</web:param1>
			         <web:param2>?</web:param2>
			      </web:method1>
			   </soapenv:Body>
			</soapenv:Envelope>
			*/
			XmlObject xmlObject = XmlUtils.createXmlObject(soapRequestBody);
			// Header
			//Element header = XmlUtils.getFirstChildElementIgnoreCase(element, "soapenv:Header");
			//Element header = (Element) SoapUtils.getHeaderElement(xmlObject, soapVersion, true);
			//System.out.println(header);
			// Body
			//Element body = XmlUtils.getFirstChildElementIgnoreCase(element, "soapenv:Body");
			Element body = (Element) SoapUtils.getBodyElement(xmlObject, soapVersion).getDomNode();
			//System.out.println(body);
			// Method Elements		
			NodeList methodNodes = body.getChildNodes();
			for (int i = 0; i < methodNodes.getLength(); ++i) {
				// Method Element
				Node methodNode = methodNodes.item(i);
				if (methodNode.getNodeType() == Node.ELEMENT_NODE) {
					//System.out.println(methodNode.getLocalName());
					NodeList paramNodes = methodNode.getChildNodes();
					for (int p = 0; p < paramNodes.getLength(); ++p) {
						// Param Element
						Node paramNode = paramNodes.item(p);
						if ( paramNode.getNodeType() == Node.ELEMENT_NODE && XmlUtils.getNodeValue(paramNode).equals("?")) {
							inputNames.add(getName(paramNode));
							inputTypes.add("String");
						}
					}
					 
				}
			}
			
			String soapPopulatedBody = XmlUtils.serialize(body.getOwnerDocument());
			return soapPopulatedBody;
			
		} catch (Exception e) {
			throw new SoapUIException(e);
		}
	}

	public static String extractResponse(String soapResponseBody, SoapVersion soapVersion, List<String> outputNames, List<String> outputType) throws SoapUIException {
		
		try {
			/*
			<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
			   <soap:Body>
			      <getCountryCityByIpResponse xmlns="http://WebXml.com.cn/">
			         <getCountryCityByIpResult>
			            <string>221.110.10.14</string>
			            <string>日本</string>
			         </getCountryCityByIpResult>
			      </getCountryCityByIpResponse>
			   </soap:Body>
			</soap:Envelope>
			*/
			XmlObject xmlObject = XmlUtils.createXmlObject(soapResponseBody);
			// Header
			//Element header = XmlUtils.getFirstChildElementIgnoreCase(element, "soapenv:Header");
			//Element header = (Element) SoapUtils.getHeaderElement(xmlObject, soapVersion, true);
			//System.out.println(header);
			// Body
			//Element body = XmlUtils.getFirstChildElementIgnoreCase(element, "soapenv:Body");
			Element body = (Element) SoapUtils.getBodyElement(xmlObject, soapVersion).getDomNode();
			//System.out.println(body);
			List<String> result = Lists.newArrayList();
			// Method Response Elements		
			NodeList methodNodes = body.getChildNodes();
			for (int i = 0; i < methodNodes.getLength(); ++i) {
				// Method Response Element	
				Node methodNode = methodNodes.item(i);
				if (methodNode.getNodeType() == Node.ELEMENT_NODE) {
					//System.out.println(methodNode.getLocalName());
					// Method Result Elements		
					NodeList resultNodes = methodNode.getChildNodes();
					for (int j = 0; j < resultNodes.getLength(); j++) {
						// Method Result Element
						Node resultNode = resultNodes.item(j);
						// Response Object Array
						NodeList respNodes = resultNode.getChildNodes();
						for (int k = 0; k < respNodes.getLength(); ++k) {
							// Response Object Element
							Node respNode = respNodes.item(k);
							if ( respNode.getNodeType() == Node.ELEMENT_NODE) {
								
								outputNames.
								
								result.add(respNode.getNodeValue());
							}
						}
					}
				}
			}
			return result.toArray(new String[result.size()]);
		} catch (Exception e) {
			throw new SoapUIException(e);
		}
	}


	/*public static void parseXML(String requestXml, List<String> inputType,
			List<String> inputNames, List<String> outputType) throws DocumentException {
		Document read = DocumentHelper.parseText(requestXml);
		Element rootElement = read.getRootElement();
		List<Element> elements = rootElement.elements();
		for (Element element : elements) {
			if ("Body".equals(element.getName())) {
				// 方法
				List<Element> elements2 = element.elements();
				for (Element element2 : elements2) {
					// 参数
					parseParam(element2, 1, 1, inputType, inputNames, outputType);
				}
			}
		}
	}
	
	public static void parseParam(Element element2, int gen, int genParent, List<String> inputType,
			List<String> inputNames, List<String> outputType) {
		if (element2 != null) {
			List<Element> elements3 = element2.elements();
			if ((elements3 != null) && (elements3.size() != 0))
				for (Element element : elements3) {
					inputType.add(gen + "," + genParent);
					inputNames.add(element.getName());
					if (element != null) {
						List e = element.elements();
						if ((e != null) && (e.size() != 0)) {
							outputType.add("1");
							int gen1 = gen + gen;
							parseParam(element, gen1, gen, inputType, inputNames, outputType);
						} else {
							outputType.add("0");
						}
					}
				}
		}
	}*/
}
