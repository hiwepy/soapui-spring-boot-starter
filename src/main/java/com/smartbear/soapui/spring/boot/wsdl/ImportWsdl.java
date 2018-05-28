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
package com.smartbear.soapui.spring.boot.wsdl;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ImportWsdl {
	
	public static Map<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();

	@SuppressWarnings("unchecked")
	public static List<WsdlMethodInfo> getProBySoap(String wsdlUrl) throws Exception {
		
		// 进行一次url有效性检查
		URL url = new URL(wsdlUrl);
		InputStream openStream = url.openStream();
		openStream.close();

		Map<String, Object> result = new HashMap<String, Object>();
		WsdlProject project = new WsdlProject();
		WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, wsdlUrl);

		WsdlInterface wsdl = wsdls[0];
		List<Operation> operationList = wsdl.getOperationList();
		for (int i = 0; i < operationList.size(); i++) {
			Operation operation = operationList.get(i);
			WsdlOperation op = (WsdlOperation) operation;
			Map<String, Object> tmp = new HashMap<String, Object>();
			tmp.put(op.getName(), op.createRequest(true));
			result.put(i + "", tmp);
		}

		if ((result != null) && (result.size() != 0)) {
			List<WsdlMethodInfo> importWsdl = new ArrayList<WsdlMethodInfo>();
			Set<String> keySet = result.keySet();
			Iterator<String> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				WsdlMethodInfo info = new WsdlMethodInfo();
				List inputType = new ArrayList();
				List inputNames = new ArrayList();
				List isparent = new ArrayList();
				// index
				String index = "" + iterator.next();
				
				Map<String, Object> hashMap = (Map<String, Object>) result.get(index);
				// method names
				Set<String> keySet2 = hashMap.keySet();
				Iterator<String> iterator2 = keySet2.iterator();
				if (iterator2.hasNext()) {
					
					String optname = (String) iterator2.next();
					String requestXml = (String) hashMap.get(optname);
					// 处理targetNameSpace
					String qname = requestXml.substring(requestXml.lastIndexOf("\"http://") + 1, requestXml.lastIndexOf("\">"));
					info.setTargetNameSpace(qname);

					String soap11 = "http://schemas.xmlsoap.org/soap/envelope";
					String soap12 = "http://www.w3.org/2003/05/soap-envelope";
					
					InputStreamReader is = new InputStreamReader(new ByteArrayInputStream(requestXml.getBytes("utf-8")));
					BufferedReader ibr = new BufferedReader(is);
					String readLine = ibr.readLine();
					if (readLine != null) {
						if (readLine.indexOf(soap11) >= 0)
							info.setTargetXsd("11");
						else if (readLine.indexOf(soap12) >= 0) {
							info.setTargetXsd("12");
						}
					}
					ibr.close();
					is.close();

					info.setSoapAction(requestXml);

					Document read = DocumentHelper.parseText(requestXml);
					Element rootElement = read.getRootElement();
					List<Element> elements = rootElement.elements();
					for (Element element : elements) {
						if ("Body".equals(element.getName())) {
							List<Element> elements2 = element.elements();
							info.setMethodName(optname);
							for (Element element2 : elements2) {
								getParameter(element2, 1, 1, inputType, inputNames, isparent);
								info.setInputNames(inputNames);
								info.setInputType(inputType);
								info.setOutputType(isparent);
							}
						}
					}

					importWsdl.add(info);
				}
			}
			return importWsdl;
		}
		return null;
	}

	public static void getParameter(Element element2, int gen, int genParent, List<String> inputType,
			List<String> inputNames, List<String> isparent) {
		if (element2 != null) {
			List<Element> elements3 = element2.elements();
			if ((elements3 != null) && (elements3.size() != 0))
				for (Element element : elements3) {
					inputType.add(gen + "," + genParent);
					inputNames.add(element.getQualifiedName());
					if (element != null) {
						List e = element.elements();
						if ((e != null) && (e.size() != 0)) {
							isparent.add("1");
							int gen1 = gen + gen;
							getParameter(element, gen1, gen, inputType, inputNames, isparent);
						} else {
							isparent.add("0");
						}
					}
				}
		}
	}
}