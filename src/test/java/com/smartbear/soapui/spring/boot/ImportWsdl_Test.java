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

import java.util.List;

import org.junit.Test;

import com.smartbear.soapui.spring.boot.wsdl.ImportWsdl;
import com.smartbear.soapui.spring.boot.wsdl.WsdlMethodInfo;

/**
 * TODO
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class ImportWsdl_Test {

	@Test
	public void testA() throws Exception {
		
		List<WsdlMethodInfo> methods = ImportWsdl.getProBySoap("http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl");
		for (WsdlMethodInfo methodInfo : methods) {
			
			System.out.println("Method Name :" + methodInfo.getMethodName());
			System.out.println("Method Desc :" + methodInfo.getMethodDesc());
			
			System.out.println("EndPoint :" + methodInfo.getEndPoint());
			System.out.println("Input Desc :" + methodInfo.getInputDesc());
			
		}
		
	}
	
}
