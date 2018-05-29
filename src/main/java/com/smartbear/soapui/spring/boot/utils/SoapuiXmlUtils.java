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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SoapuiXmlUtils {
	
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
