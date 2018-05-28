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

import java.io.ByteArrayInputStream;
import java.util.Map;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.submit.RequestTransportRegistry;
import com.eviware.soapui.impl.wsdl.submit.transports.http.support.methods.ExtendedPostMethod;
import com.eviware.soapui.impl.wsdl.support.http.HttpClientSupport;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlContext;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.handler.SoapRequestHandler;
import com.smartbear.soapui.spring.boot.handler.SoapResponseHandler;
import com.smartbear.soapui.spring.boot.wsdl.WsdlInfo;

/**
 * 
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapuiRequestTemplate {

	private SoapuiWsdlTemplate wsdlTemplate;
	private SoapRequestHandler requestHandler;
	
	public SoapuiRequestTemplate(SoapuiWsdlTemplate wsdlTemplate, SoapRequestHandler requestHandler) {
		this.wsdlTemplate = wsdlTemplate;
		this.requestHandler = requestHandler;
	}
	
	public Map<String, String> sendRequest(String operation, Map<String, Object> params, String wsdlUrl)
			throws Exception {
		int index = wsdlUrl.indexOf("?wsdl");
		String address = wsdlUrl.substring(0, index);
		return sendRequest(address, operation, params, wsdlUrl);
	}

	public Map<String, String> sendRequest(String address, String operation, Map<String, Object> params, String wsdlUrl)
			throws Exception {
		
		
		RequestTransportRegistry.getTransport(endpoint, submitContext)
		
		
		ExtendedPostMethod method = new ExtendedPostMethod(address);
		
		HttpClientSupport.execute(method);
		
		
		
		
		Operation operationInst = getOperation(wsdlUrl, operation, null);

		String message = buildRequest(wsdlUrl, operationInst, params, null, null, null);

		Map responseMap = populateResponseOgnlMap(sendRequest(address, message, operationInst.getAction()));

		return responseMap;
	}

	private String sendRequest(String address, String message, String action) throws Exception {
		PostMethod postMethod = new PostMethod(address);
		String responseBodyAsString;
		try {
			postMethod.setRequestHeader("SOAPAction", action);
			postMethod.setRequestEntity(
					new InputStreamRequestEntity(new ByteArrayInputStream(message.getBytes("UTF-8")), "text/xml"));

			this.client.executeMethod(postMethod);
			responseBodyAsString = postMethod.getResponseBodyAsString();
		} finally {
			postMethod.releaseConnection();
		}

		return responseBodyAsString;
	}
	
}
