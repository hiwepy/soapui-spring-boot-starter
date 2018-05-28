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

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {
	public static String serialize(NodeList nodeList) throws DOMException {
		return serialize(nodeList, false);
	}

	public static String serialize(Node node, boolean format) throws DOMException {
		StringWriter writer = new StringWriter();
		serialize(node, format, writer);
		return writer.toString();
	}

	public static void serialize(Node node, boolean format, Writer writer) throws DOMException {
		if (node.getNodeType() == 9)
			serialize(node.getChildNodes(), format, writer);
		else
			serialize(new NodeList() {
				public Node item(int index) {
					return node;
				}

				public int getLength() {
					return 1;
				}
			}, format, writer);
	}

	public static String serialize(NodeList nodeList, boolean format) throws DOMException {
		StringWriter writer = new StringWriter();
		serialize(nodeList, format, writer);
		return writer.toString();
	}

	public static void serialize(NodeList nodeList, boolean format, Writer writer) throws DOMException {
		if (nodeList == null) {
			throw new IllegalArgumentException("null 'subtree' NodeIterator arg in method call.");
		}
		try {
			TransformerFactory factory = TransformerFactory.newInstance();

			if (format) {
				try {
					factory.setAttribute("indent-number", new Integer(4));
				} catch (Exception localException1) {
				}
			}
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty("omit-xml-declaration", "yes");
			if (format) {
				transformer.setOutputProperty("indent", "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
			}

			int listLength = nodeList.getLength();

			for (int i = 0; i < listLength; ++i) {
				Node node = nodeList.item(i);

				if (isTextNode(node))
					writer.write(node.getNodeValue());
				else if (node.getNodeType() == 2)
					writer.write(((Attr) node).getValue());
				else if (node.getNodeType() == 1)
					transformer.transform(new DOMSource(node), new StreamResult(writer));
			}
		} catch (Exception e) {
			DOMException domExcep = new DOMException((short) 15, "Unable to serailise DOM subtree.");
			domExcep.initCause(e);
			throw domExcep;
		}
	}

	public static boolean isTextNode(Node node) {
		if (node == null) {
			return false;
		}
		short nodeType = node.getNodeType();

		return ((nodeType == 4) || (nodeType == 3));
	}
	
	public static void parseXML(String requestXml, List<String> inputType,
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
					inputNames.add(element.getQualifiedName());
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
	}
}