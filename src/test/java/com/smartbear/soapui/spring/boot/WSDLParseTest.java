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
package com.smartbear.soapui.spring.boot;

/**
 * 
 * http://www.cnblogs.com/coshaho/p/5105545.html
 * http://www.cnblogs.com/coshaho/p/5689738.html
 * 
 * WSDLParseTest.java Create on 2016年7月20日 下午9:24:36
 * 
 * 类功能说明: WSDL解析测试
 *
 * Copyright: Copyright(c) 2013 Company: COSHAHO
 * 
 * @Version 1.0
 * @Author 何科序
 */
public class WSDLParseTest {
	
	public static void main(String[] args) throws Exception {
		String url = "http://webservice.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx?wsdl";
		WsdlInfo wsdlInfo = new WsdlInfo(url);
		System.out.println("WSDL URL is " + wsdlInfo.getWsdlName());

		for (InterfaceInfo interfaceInfo : wsdlInfo.getInterfaces()) {
			System.out.println("Interface name is " + interfaceInfo.getInterfaceName());
			for (String ads : interfaceInfo.getAdrress()) {
				System.out.println("Interface address is " + ads);
			}
			for (OperationInfo operation : interfaceInfo.getOperations()) {
				System.out.println("operation name is " + operation.getOperationName());
				System.out.println("operation request is ");
				System.out.println("operation request is " + operation.getRequestXml());
				System.out.println("operation response is ");
				System.out.println(operation.getResponseXml());
			}
		}
	}
}