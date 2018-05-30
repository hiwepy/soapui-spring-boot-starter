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
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;

import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.google.common.collect.Maps;

public class SoapuiRequestTemplate_Test extends AbstractWsdlTemplate_Test {
	
	protected SoapuiRequestTemplate requestTemplate;
	
	@Before
	public void setupRequestTemplate() throws XmlException, IOException, SoapUIException {
		requestTemplate = new SoapuiRequestTemplate(wsdlTemplate);
	}
	

	//@Test
	public void invokeAt() throws XmlException, IOException, SoapUIException, SubmitException {

		//SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
		
		System.err.println( "invokeAt===========================================");
		
		Map<String, Object> params = Maps.newHashMap();
		
		params.put("theIpAddress",  "221.110.10.14");
		
		Response response = requestTemplate.invokeAt(wsdlUrl, 1, params);
		
		System.out.println(response.getResponseHeaders());
		
		System.out.println(response.getContentAsString());
		
		
	}
	
	@Test
	public void invokeByName() throws XmlException, IOException, SoapUIException, SubmitException {
		
		//SoapUI.getSettings().setBoolean(HttpSettings.DISABLE_RESPONSE_DECOMPRESSION, true);
		
		System.err.println( "invokeByName===========================================");
		
		//Map<String, Object> params = Maps.newHashMap();
		//params.put("theIpAddress",  "221.110.10.14");
		
		Response response = requestTemplate.invokeByName(wsdlUrl, "getCountryCityByIp", "221.110.10.14");
		
		System.out.println(response.getResponseHeaders());
		
		System.out.println(response.getContentAsString());
		
	}
	
}
