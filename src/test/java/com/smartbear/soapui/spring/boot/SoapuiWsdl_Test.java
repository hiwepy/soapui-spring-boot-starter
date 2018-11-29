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

import com.smartbear.soapui.template.wsdl.WsdlInfo;
import com.smartbear.soapui.template.wsdl.WsdlInterfaceInfo;
import com.smartbear.soapui.template.wsdl.WsdlOperationInfo;

/**
 * http://www.cnblogs.com/coshaho/p/5105545.html
 * http://www.cnblogs.com/coshaho/p/5689738.html
 */
public class SoapuiWsdl_Test {
	
	public static void main(String[] args) throws Exception {
        
		String wsdlUrl = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
		WsdlInfo wsdlInfo = new WsdlInfo(wsdlUrl);
		System.out.println("WSDL URL is " + wsdlInfo.getWsdlUrl());

		for (WsdlInterfaceInfo interfaceInfo : wsdlInfo.getInterfaces()) {
			System.out.println("Interface name is " + interfaceInfo.getInterfaceName());
			for (String ads : interfaceInfo.getAdrress()) {
				System.out.println("Interface address is " + ads);
			}
			for (WsdlOperationInfo operation : interfaceInfo.getOperations()) {
				
				
				System.out.println("operation name is " + operation.getOperationName());
				System.out.println("operation request is ");
				System.out.println("operation request is " + operation.getRequestXml());
				System.out.println("operation response is ");
				System.out.println(operation.getResponseXml());
			}
		}
	}
}