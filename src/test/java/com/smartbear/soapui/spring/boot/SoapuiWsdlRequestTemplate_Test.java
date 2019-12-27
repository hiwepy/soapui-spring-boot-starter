/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.support.SoapUIException;
import com.google.common.collect.Maps;
import com.smartbear.soapui.template.SoapuiResponse;
import com.smartbear.soapui.template.SoapuiWsdlRequestTemplate;
import com.smartbear.soapui.template.handler.SoapResponseHandler;
import com.smartbear.soapui.template.handler.def.SoapXMLResponseHandler;
import com.smartbear.soapui.template.setting.SoapuiSettings;
import com.smartbear.soapui.template.setting.SoapuiSettingsImpl;

public class SoapuiWsdlRequestTemplate_Test extends AbstractWsdlTemplate_Test {
	
	protected SoapuiWsdlRequestTemplate requestTemplate;
	protected SoapResponseHandler<String> handler = new SoapXMLResponseHandler();
	
	@Before
	public void setupRequestTemplate() throws Exception {
		// 替换默认Settings
		new SoapuiSettingsImpl(new SoapuiSettings(), SoapUI.getSettings());
		requestTemplate = new SoapuiWsdlRequestTemplate(wsdlTemplate);
	}
	
	//@Test
	public void invokeAt() throws XmlException, IOException, SoapUIException, SubmitException {

		//SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
		
		System.err.println( "invokeAt===========================================");
		
		Map<String, Object> params = Maps.newHashMap();
		
		params.put("theIpAddress",  "221.110.10.14");
		
		SoapuiResponse<WsdlRequest> response = requestTemplate.invokeAt(wsdlUrl, 1, params);
		
		System.out.println(response.getResponse().getResponseHeaders());
		
		System.out.println(response.getResponse().getContentAsString());
		
	}
	
	//@Test
	public void invokeByName() throws XmlException, IOException, SoapUIException, SubmitException {
		
		//SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
		
		System.err.println( "invokeByName===========================================");
		
		//Map<String, Object> params = Maps.newHashMap();
		//params.put("theIpAddress",  "221.110.10.14");
		
		SoapuiResponse<WsdlRequest> response = requestTemplate.invokeByName(wsdlUrl, "getCountryCityByIp", new String[] {"221.110.10.14"});
		
		System.out.println(response.getResponse().getResponseHeaders());
		
		System.out.println(response.getResponse().getContentAsString());
		
	}
	
	@Test
	public void invokeByName2() throws XmlException, IOException, SoapUIException, SubmitException {
		
		//SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
		
		System.err.println( "invokeByName===========================================");
		
		//Map<String, Object> params = Maps.newHashMap();
		//params.put("theIpAddress",  "221.110.10.14");
		wsdlUrl = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl";
		Map<String, Object> requestParams = new HashMap<>();
		requestParams.put("mobileCode", "13735893463");
		
		SoapuiResponse<WsdlRequest> response = requestTemplate.invokeByName(wsdlUrl, "getMobileCodeInfo", requestParams);
		
		System.out.println(response.getResponse().getResponseHeaders());
		
		System.out.println(response.getResponse().getContentAsString());
		
	}
	
}
