/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

import java.io.IOException;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.support.xml.XmlUtils;

/**
 * TODO
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */

public class SoapuiMessageParse_Test {

	/**
	 * 多结果
	 */
	public void testMultiResult() throws IOException, Exception {
		
		String responseContent = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebXml.com.cn/\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <web:getCountryCityByIpResponse>\r\n" + 
				"         <!--Optional:-->\r\n" + 
				"         <web:getCountryCityByIpResult>\r\n" + 
				"            <!--Zero or more repetitions:-->\r\n" + 
				"            <web:string>?</web:string>\r\n" + 
				"         </web:getCountryCityByIpResult>\r\n" + 
				"      </web:getCountryCityByIpResponse>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		XmlObject xmlObject = XmlUtils.createXmlObject(responseContent);
		// Header
		//Element header = XmlUtils.getFirstChildElementIgnoreCase(element, "soapenv:Header");
		//Element header = (Element) SoapUtils.getHeaderElement(xmlObject, soapVersion, true);
		//System.out.println(header);
		// Body
		//Element body = XmlUtils.getFirstChildElementIgnoreCase(element, "soapenv:Body");
		Element body = (Element) SoapUtils.getBodyElement(xmlObject, SoapVersion.Soap11).getDomNode();
		System.out.println(body.getLocalName());
		//System.out.println(body);
		
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
							
							XmlUtils.setNodeValue(respNode, "546454");
							
							System.out.println(respNode.getNodeName());
							System.out.println(respNode.getLocalName());
							System.out.println(respNode.getNodeValue());
							
						}
					}
				}
			}
		}
		
		 
		System.out.println(XmlUtils.serialize(body.getOwnerDocument()));
		
		/*String soapRequestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"namespace\"><soapenv:Header/><soapenv:Body><web:method1><web:param1>?</web:param1><web:param2>?</web:param2></web:method1></soapenv:Body></soapenv:Envelope>";
		Document messageDoc = XmlUtils.parseXml(soapRequestBody);
		Element docRoot = messageDoc.getDocumentElement();
		
		Node node = docRoot.getFirstChild();
		
		System.out.println(XmlUtils.getElementPath(docRoot));
		
		System.out.println(SoapuiXmlUtils.getNode(node, "/web:method1[1]"));;*/
		System.exit(0);
	}
}
