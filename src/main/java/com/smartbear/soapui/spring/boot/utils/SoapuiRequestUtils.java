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

import java.io.PrintWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.xml.XmlUtils;

public class SoapuiRequestUtils {
	
	private static final Logger LOGGER = Logger.getLogger(SoapuiRequestUtils.class);
	
	public static String buildSoapMessage(String soapRequestBody, Map<String, Object> params,
			String soapNs) throws SoapUIException {
		
		try {
			
			Document messageDoc = XmlUtils.parseXml(soapRequestBody);
			
			Element docRoot = messageDoc.getDocumentElement();

			boolean dumpSoap = params.containsKey("dumpSoap");
			if (dumpSoap) {
				dumpSoap("Soap Template (Unexpanded):", docRoot);
			}

			// 从doc根节点开始进行值填充
			injectParameters(docRoot, params, soapNs);

			if (dumpSoap) {
				dumpSoap("Soap Message (Populated Template):", docRoot);
			}

			return XmlUtils.serialize(messageDoc);
		} catch (Exception e) {
			throw new SoapUIException(e);
		}
	}
	

	private static void dumpSoap(String message, Element docRoot) {
		System.out.println(message + "\n");
		try {
			XmlUtils.serialize(docRoot, new PrintWriter(System.out));
		} catch (Exception e) {
			LOGGER.error("Unable to dump Soap.", e);
		}
	}
	
	public static void injectParameters(Element element, Map<String, Object> params, String soapNs) {
		
		
		
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
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			
			Node node = children.item(i);
			
			// Header
			
			
			// Body
			
			if ("Body".equalsIgnoreCase(SoapuiXmlUtils.getName(node)) && node.getNodeType() == Node.ELEMENT_NODE) {
				
				NodeList paramNodes = node.getChildNodes();
				for (int p = 0; p < paramNodes.getLength(); ++p) {
					
					Node paramNode = paramNodes.item(p);
					if ( paramNode.getNodeType() == Node.ELEMENT_NODE && paramNode.getNodeValue().equals("?") ) {
						String ognl = OGNLUtils.getOGNLExpression(element, soapNs);
						Object param = params.get(ognl);
						paramNode.setNodeValue(param.toString());
						/*
						element.removeChild(node);
						element.appendChild(element.getOwnerDocument().createTextNode(param.toString()));*/
					}
				}
				 
			}
		}
	}
	
	public static void injectParameters2(Element element, Map<String, Object> params, String soapNs) {
		
		NodeList children = element.getChildNodes();
		int childCount = children.getLength();

		for (int i = 0; i < childCount; ++i) {
			Node node = children.item(i);

			if ((childCount == 1) && (node.getNodeType() == 3)) {
				if (node.getNodeValue().equals("?")) {
					String ognl = OGNLUtils.getOGNLExpression(element, soapNs);
					Object param = params.get(ognl);

					element.removeChild(node);
					element.appendChild(element.getOwnerDocument().createTextNode(param.toString()));
				} else if (node.getNodeType() == Node.ELEMENT_NODE) {
					injectParameters((Element) node, params, soapNs);
				}
			}
		}
	}


	
}