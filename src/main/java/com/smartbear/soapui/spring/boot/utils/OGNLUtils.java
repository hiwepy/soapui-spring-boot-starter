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

import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.eviware.soapui.support.xml.XmlUtils;

public class OGNLUtils {
	
	public static final String IS_COLLECTION_ATTRIB = "is-collection";

	public static String getOGNLExpression(Element element) {
		
		StringBuffer ognlExpression = new StringBuffer();
		
		Node parent = element.getParentNode();
		
		boolean isInBody = false;

		ognlExpression.append(getOGNLToken(element));

		while ((parent != null) && (parent.getNodeType() == 1)) {
			
			Element parentElement = (Element) parent;
			String parentName = SoapuiXmlUtils.getName(parentElement);

			if ((parentName.equalsIgnoreCase("body"))
					&& (parent.getNamespaceURI().equalsIgnoreCase("http://schemas.xmlsoap.org/soap/envelope/"))) {
				isInBody = true;
				break;
			}

			ognlExpression.insert(0, getOGNLToken(parentElement));
			parent = parent.getParentNode();
		}

		if (!(isInBody)) {
			return "";
		}

		ognlExpression.deleteCharAt(0);

		return ognlExpression.toString();
	}

	public static String getOGNLExpression(Element element, String nameSpace) {
		StringBuffer ognlExpression = new StringBuffer();
		Node parent = element.getParentNode();
		boolean isInBody = false;

		ognlExpression.append(getOGNLToken(element));

		while ((parent != null) && (parent.getNodeType() == 1)) {
			
			Element parentElement = (Element) parent;
			
			String parentName = SoapuiXmlUtils.getName(parentElement);
			if ((parentName.equalsIgnoreCase("body")) && (checkParentNameSpace(parent.getNamespaceURI(), nameSpace))) {
				isInBody = true;
				break;
			}

			ognlExpression.insert(0, getOGNLToken(parentElement));
			parent = parent.getParentNode();
		}

		if (!(isInBody)) {
			return "";
		}

		ognlExpression.deleteCharAt(0);

		return ognlExpression.toString();
	}

	protected static boolean checkParentNameSpace(String parentNS, String namespace) {
		if (parentNS == null) {
			return false;
		}
		/*SOAPNameSpaces[] defaultNamespaces = SOAPNameSpaces.values();
		for (SOAPNameSpaces defaultNS : defaultNamespaces) {
			if (parentNS.equalsIgnoreCase(defaultNS.getNameSpace())) {
				return true;
			}
		}*/
		return parentNS.equalsIgnoreCase(namespace);
	}

	public static String getOGNLToken(Element element) {
		
		String localName = element.getLocalName();
		String ognlToken;
		if (assertIsParentCollection(element)) {
			int count = SoapuiXmlUtils.countElementsBefore(element, element.getTagName());
			ognlToken = "[" + count + "]";
		} else {
			ognlToken = "." + localName;
		}
		
		return ognlToken;
	}

	private static boolean assertIsCollection(Element element) {
		Comment firstComment = (Comment) SoapuiXmlUtils.getFirstChildByType(element, Node.COMMENT_NODE);
		return ((firstComment != null) && (firstComment.getNodeValue().indexOf("1 or more repetitions") != -1));
	}

	private static boolean assertIsParentCollection(Element element) {
		Node parent = element.getParentNode();
		return ((parent != null) && (parent.getNodeType() == 1) && (assertIsCollection((Element) parent)));
	}
 
}