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

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.xml.XmlUtils;
import com.google.common.collect.Lists;

public class SoapuiResponseUtils {
	
	public static String[] parseResponseToArray(String soapResponseBody, SoapVersion soapVersion) throws SoapUIException {
		
		try {
			/*
			At Least One Result：
			<?xml version="1.0" encoding="utf-8"?>
			<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
			  <soap:Body>
			    <getWeatherbyCityNameResponse xmlns="http://WebXml.com.cn/">
			      <getWeatherbyCityNameResult>
			        <string>浙江</string>
					<string>杭州</string>
					<string>58457</string>
					<string>58457.jpg</string>
					<string>2018/5/30 10:17:08</string>
					<string>20℃/28℃</string>
					<string>5月30日 阵雨转中雨</string>
					<string>东风转东北风小于3级</string>
			      </getWeatherbyCityNameResult>
			    </getWeatherbyCityNameResponse>
			  </soap:Body>
			</soap:Envelope>
			No Result：
			<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:web="http://WebXml.com.cn/">
   				<soap:Header/>
			   	<soap:Body>
			      	<web:getVersionTimeResponse>
			         	<!--Optional:-->
			         	<web:getVersionTimeResult/>
			      	</web:getVersionTimeResponse>
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
						// At Least One Result
						if ( SoapuiXmlUtils.countChildElementsOfType(resultNode, Node.ELEMENT_NODE) > 0) {
							// Response Object Array
							NodeList respNodes = resultNode.getChildNodes();
							for (int k = 0; k < respNodes.getLength(); ++k) {
								// Response Object Element
								Node respNode = respNodes.item(k);
								if ( respNode.getNodeType() == Node.ELEMENT_NODE) {
									result.add(XmlUtils.getNodeValue(respNode));
								}
							}
						} else if(resultNode.getNodeType() == Node.ELEMENT_NODE ) {
							result.add(XmlUtils.getNodeValue(resultNode));
						}
					}
				}
			}
			return result.toArray(new String[result.size()]);
		} catch (Exception e) {
			throw new SoapUIException(e);
		}
		
	}

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

}
