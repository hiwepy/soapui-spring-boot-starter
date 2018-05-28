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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;
import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMUtil {
	
	private static String ELEMENT_NAME_FUNC = "/name()";

	private static XPathFactory xPathFactory = XPathFactory.newInstance();

	public static Document createDocument() throws ConfigurationException {
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException("Failed to create ESB Configuration Document instance.");
		}

		return doc;
	}

	public static Document parse(String xml) throws SAXException, IOException {
		return parseStream(new ByteArrayInputStream(xml.getBytes()), false, false);
	}

	public static Document parseStream(InputStream stream, boolean validate, boolean expandEntityRefs)
			throws SAXException, IOException {
		return parseStream(stream, validate, expandEntityRefs, false);
	}

	public static Document parseStream(InputStream stream, boolean validate, boolean expandEntityRefs,
			boolean namespaceAware) throws SAXException, IOException {
		if (stream == null)
			throw new IllegalArgumentException("null 'stream' arg in method call.");
		IllegalStateException state;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;

			factory.setValidating(validate);
			factory.setExpandEntityReferences(expandEntityRefs);
			factory.setNamespaceAware(namespaceAware);
			docBuilder = factory.newDocumentBuilder();

			return docBuilder.parse(stream);
		} catch (ParserConfigurationException e) {
			state = new IllegalStateException("Unable to parse XML stream - XML Parser not configured correctly.");
			state.initCause(e);
			throw state;
		} catch (FactoryConfigurationError e) {
			state = new IllegalStateException(
					"Unable to parse XML stream - DocumentBuilderFactory not configured correctly.");
			state.initCause(e);
			throw state;
		}
	}

	public static String getAttribute(Element element, String name, String defaultVal) {
		if (element.hasAttribute(name)) {
			return element.getAttribute(name);
		}
		return defaultVal;
	}

	public static Element addElement(Node parent, String elementName) {
		Element element = null;

		if (parent instanceof Document)
			element = ((Document) parent).createElement(elementName);
		else {
			element = parent.getOwnerDocument().createElement(elementName);
		}
		parent.appendChild(element);

		return element;
	}

	public static void removeEmptyAttributes(Element element) {
		NamedNodeMap attributes = element.getAttributes();
		int attribCount = attributes.getLength();

		for (int i = attribCount - 1; i >= 0; --i) {
			Attr attribute = (Attr) attributes.item(i);

			if (attribute.getValue().equals(""))
				attributes.removeNamedItem(attribute.getName());
		}
	}

	public static void serialize(Node node, File outdir, String fileName) throws ConfigurationException {
		serialize(node, new StreamResult(new File(outdir, fileName)));
	}

	public static void serialize(Node node, OutputStream out) throws ConfigurationException {
		serialize(node, new StreamResult(out));
	}

	public static void serialize(Node node, StreamResult streamRes) throws ConfigurationException {
		serialize(node, streamRes, false);
	}

	public static void serialize(Node node, StreamResult streamRes, boolean omitXmlDecl) throws ConfigurationException {
		DOMSource domSource = new DOMSource(node);
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			transformer.setOutputProperty("indent", "yes");
			transformer.setOutputProperty("omit-xml-declaration", (omitXmlDecl) ? "yes" : "no");
			transformer.transform(domSource, streamRes);
		} catch (Exception e) {
			throw new ConfigurationException("Failed to serialize ESB Configuration Document instance.");
		}
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

	public static List<Node> copyNodeList(NodeList nodeList) {
		List copy = new Vector();

		if (nodeList != null) {
			int nodeCount = nodeList.getLength();

			for (int i = 0; i < nodeCount; ++i) {
				copy.add(nodeList.item(i));
			}
		}

		return copy;
	}

	public static Element getNextSiblingElement(Node node) {
		Node nextSibling = node.getNextSibling();

		while (nextSibling != null) {
			if (nextSibling.getNodeType() == 1) {
				return ((Element) nextSibling);
			}
			nextSibling = nextSibling.getNextSibling();
		}

		return null;
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

	public static String getName(Element element) {
		String name = element.getLocalName();

		if (name != null) {
			return name;
		}
		return element.getTagName();
	}

	public static void copyChildNodes(Node source, Node target) {
		List nodeList = copyNodeList(source.getChildNodes());
		int childCount = nodeList.size();

		for (int i = 0; i < childCount; ++i)
			target.appendChild((Node) nodeList.get(i));
	}
}