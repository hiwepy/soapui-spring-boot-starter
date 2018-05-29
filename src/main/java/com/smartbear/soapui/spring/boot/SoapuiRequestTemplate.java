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

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.handler.SoapResponseHandler;
import com.smartbear.soapui.spring.boot.utils.SoapuiRequestUtils;

/**
 * 
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapuiRequestTemplate {

	private SoapuiWsdlTemplate wsdlTemplate;
	
	public SoapuiRequestTemplate(SoapuiWsdlTemplate wsdlTemplate) {
		this.wsdlTemplate = wsdlTemplate;
	}
	
	public Response invokeAt(String wsdlUrl, int index, Map<String, Object> params) throws SoapUIException, SubmitException {
		
		// get desired operation
		WsdlOperation operationInst = wsdlTemplate.getOperationAt(wsdlUrl ,index);
		
		// create a new empty request for that operation
		String requestName = "Request" + DigestUtils.md5Hex(wsdlUrl + "$operation-" + index);
		WsdlRequest request = operationInst.getRequestByName(requestName);
		if(null == request) {
			request = operationInst.addNewRequest(requestName);
		}
		
		//MessageExchange exchange = new WsdlResponseMessageExchange(request);
        //MessageExchange exchange = new RestResponseMessageExchange(request);
        //MessageExchange exchange = new HttpResponseMessageExchange(request);
        
		// generate the request content from the schema
		String requestXML = operationInst.createRequest( true );
		
		
		
		
		
		
		
		// 处理targetNameSpace
		String soapNs = requestXML.substring(requestXML.lastIndexOf("\"http://") + 1, requestXML.lastIndexOf("\">"));
		
		// 对 requestContent 进行动态补全
		String soapMessage = SoapuiRequestUtils.buildSoapMessage(requestXML, params, soapNs);
		
		// 对 requestContent 进行动态补全
		request.setRequestContent( soapMessage );
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		return submit.getResponse();
		
	}
	
	public <T> T invokeAt(String wsdlUrl, int index, Map<String, Object> params, SoapResponseHandler<T> handler) throws SoapUIException, SubmitException {
		// wait for the response
		Response response =  this.invokeAt(wsdlUrl, index, params);
		// handle the response
		return handler.handleResponse(response);
	}
	
	public Response invokeByName(String wsdlUrl, String operationName, Map<String, Object> params) throws SoapUIException, SubmitException {
		
		// get desired operation
		WsdlOperation operationInst = wsdlTemplate.getOperationByName(wsdlUrl, operationName);
		
		// create a new empty request for that operation
		String requestName = "Request" + DigestUtils.md5Hex(wsdlUrl + "$" + operationName);
		WsdlRequest request = operationInst.getRequestByName(requestName);
		if(null == request) {
			request = operationInst.addNewRequest(requestName);
		}
		
		//MessageExchange exchange = new WsdlResponseMessageExchange(request);
        //MessageExchange exchange = new RestResponseMessageExchange(request);
        //MessageExchange exchange = new HttpResponseMessageExchange(request);
        
		// generate the request content from the schema
		String requestXML = operationInst.createRequest( true );
		
		// 处理targetNameSpace
		String soapNs = requestXML.substring(requestXML.lastIndexOf("\"http://") + 1, requestXML.lastIndexOf("\">"));
		
		// 对 requestContent 进行动态补全
		String soapMessage = SoapuiRequestUtils.buildSoapMessage(requestXML, params, soapNs);
		
		// 对 requestContent 进行动态补全
		request.setRequestContent( soapMessage );
 		
		//WsdlRequestConfig config = new WsdlRequestConfigImpl(null);
		//request.setConfig(config);
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);

		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		return submit.getResponse();
		
	}
	
	public <T> T invokeByName(String wsdlUrl, String operationName, Map<String, Object> params, SoapResponseHandler<T> handler) throws SoapUIException, SubmitException {
		// wait for the response
		Response response =  this.invokeByName(wsdlUrl, operationName, params);
		// handle the response
		return handler.handleResponse(response);
	}
	
}
