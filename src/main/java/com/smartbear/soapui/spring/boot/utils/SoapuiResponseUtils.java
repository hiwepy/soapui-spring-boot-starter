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

import java.io.IOException;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.support.xml.XmlUtils;
import com.google.common.collect.Maps;

public class SoapuiResponseUtils {

	public static String getFaultCode(SoapVersion soapVersion, String responseContent) {
		try {
			XmlObject xmlObject = XmlUtils.createXmlObject(responseContent);
			Element body = (Element) SoapUtils.getBodyElement(xmlObject, soapVersion).getDomNode();
			Element soapenvFault = XmlUtils.getFirstChildElementNS(body, "http://schemas.xmlsoap.org/soap/envelope/",
					"Fault");
			Element faultCode = XmlUtils.getFirstChildElement(soapenvFault, "faultcode");
			if (faultCode != null) {
				return XmlUtils.getElementText(faultCode);
			}
		} catch (XmlException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> populateResponseMap(String soapResponseXML) {

		Map<String, String> resultMap = Maps.newLinkedHashMap();
		try {

			Document doc = XmlUtils.parseXml(soapResponseXML);

			Element graphRootElement = getGraphRootElement(doc.getDocumentElement());

			populateResponseOgnlMap(resultMap, graphRootElement);

		} catch (IOException e) {
			throw new RuntimeException("Unexpected error reading SOAP response.", e);
		}

		return resultMap;
	}

	private static void populateResponseOgnlMap(Map<String, String> map, Element element) {
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

	private static Element getGraphRootElement(Element element) {

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

}
