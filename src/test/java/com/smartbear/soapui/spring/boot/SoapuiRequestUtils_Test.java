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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smartbear.soapui.template.utils.SoapuiRequestUtils;

public class SoapuiRequestUtils_Test {

	/**
	 * At Least One Param
	 */
	@Test
	public void testLeastOneParam() throws IOException, Exception {
		System.err.println("========================================");	
		String soapRequestBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"  <soap:Body>\r\n" + 
				"    <getWeatherbyCityName xmlns=\"http://WebXml.com.cn/\">\r\n" + 
				"      <theCityName>?</theCityName>\r\n" + 
				"    </getWeatherbyCityName>\r\n" + 
				"  </soap:Body>\r\n" + 
				"</soap:Envelope>";
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("theCityName", "杭州");
		
		String soapMessage1 = SoapuiRequestUtils.buildSoapMessage(soapRequestBody, SoapVersion.Soap11, params);
		System.out.println(soapMessage1);
		
		String soapMessage2 = SoapuiRequestUtils.buildSoapMessage(soapRequestBody, SoapVersion.Soap11, "杭州");
		System.out.println(soapMessage2);
		
	}
	
	/**
	 * No Param
	 */
	@Test
	public void testNoParam() throws IOException, Exception {
	
		System.err.println("========================================");	
		String soapRequestBody = "\r\n" + 
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"  <soap:Body>\r\n" + 
				"    <getSupportProvince xmlns=\"http://WebXml.com.cn/\" />\r\n" + 
				"  </soap:Body>\r\n" + 
				"</soap:Envelope>";
		
		Map<String, Object> params = Maps.newHashMap();
		
		String soapMessage1 = SoapuiRequestUtils.buildSoapMessage(soapRequestBody, SoapVersion.Soap11, params);
		System.out.println(soapMessage1);
		
		String soapMessage2 = SoapuiRequestUtils.buildSoapMessage(soapRequestBody, SoapVersion.Soap11);
		System.out.println(soapMessage2);
		
		
	}
	
	/**
	 * Extract Request
	 */
	@Test
	public void testExtractRequest1() throws IOException, Exception {
		System.err.println("========================================");	
		String soapRequestBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"  <soap:Body>\r\n" + 
				"    <getWeatherbyCityName xmlns=\"http://WebXml.com.cn/\">\r\n" + 
				"      <theCityName>?</theCityName>\r\n" + 
				"    </getWeatherbyCityName>\r\n" + 
				"  </soap:Body>\r\n" + 
				"</soap:Envelope>";
		
		List<String> inputNames = Lists.newArrayList();
		List<String> inputTypes = Lists.newArrayList();
		
		System.out.println(soapRequestBody);
		
		SoapuiRequestUtils.extractRequest(soapRequestBody, SoapVersion.Soap11, inputNames, inputTypes);
		
		System.out.println( "InputNames : " + inputNames);
		System.out.println( "InputTypes : " + inputTypes);
		
	}
	
	/**
	 * Extract Request
	 */
	@Test
	public void testExtractRequest2() throws IOException, Exception {
		System.err.println("========================================");	
		String soapRequestBody = "\r\n" + 
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
				"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"  <soap:Body>\r\n" + 
				"    <getSupportProvince xmlns=\"http://WebXml.com.cn/\" />\r\n" + 
				"  </soap:Body>\r\n" + 
				"</soap:Envelope>";
		
		List<String> inputNames = Lists.newArrayList();
		List<String> inputTypes = Lists.newArrayList();
		
		System.out.println(soapRequestBody);
		
		SoapuiRequestUtils.extractRequest(soapRequestBody, SoapVersion.Soap11, inputNames, inputTypes);
		
		System.out.println( "InputNames : " + inputNames);
		System.out.println( "InputTypes : " + inputTypes);
	}
	
}
