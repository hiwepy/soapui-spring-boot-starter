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

import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.xml.XmlUtils;

public class SoapuiRequestUtils {
	
	private static void dumpSoap(String message, String soapRequestBody) {
		System.out.println(message + "\n");
		System.out.println(soapRequestBody);
	}
	
	public static String buildSoapMessage(String soapRequestBody, SoapVersion soapVersion, Map<String, Object> params) throws SoapUIException {

		try {
			
			boolean dumpSoap = SoapUI.getSettings().getBoolean("dumpSoap", false);
			if (dumpSoap) {
				dumpSoap("Soap Template (Unexpanded):", soapRequestBody);
			}
			
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
							String param = (String) params.get(paramNode.getLocalName());
							// Param Element Only One Text Node
							paramNode.replaceChild(body.getOwnerDocument().createTextNode(param), paramNode.getFirstChild());
						}
					}
					 
				}
			}
			
			String soapPopulatedBody = XmlUtils.serialize(body.getOwnerDocument());
			if (dumpSoap) {
				dumpSoap("Soap Message (Populated Template):", soapPopulatedBody);
			}
			return soapPopulatedBody;
			
		} catch (Exception e) {
			throw new SoapUIException(e);
		}
	}
	
	public static String buildSoapMessage(String soapRequestBody, SoapVersion soapVersion, String[] params) throws SoapUIException {
		
		try {
			
			boolean dumpSoap = SoapUI.getSettings().getBoolean("dumpSoap", false);
			if (dumpSoap) {
				dumpSoap("Soap Template (Unexpanded):", soapRequestBody);
			}
			
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
							int index = Math.max(XmlUtils.getElementIndex(paramNode) - 1, 0);
							// Param Element Only One Text Node
							paramNode.replaceChild(body.getOwnerDocument().createTextNode(params[index]), paramNode.getFirstChild());
						}
					}
					 
				}
			}
			
			String soapPopulatedBody = XmlUtils.serialize(body.getOwnerDocument());
			if (dumpSoap) {
				dumpSoap("Soap Message (Populated Template):", soapPopulatedBody);
			}
			return soapPopulatedBody;
			
		} catch (Exception e) {
			throw new SoapUIException(e);
		}
	}
	


	
}