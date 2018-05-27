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

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlContext;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.handler.ResponseHandler;
import com.smartbear.soapui.spring.boot.wsdl.WsdlInfo;

/**
 * 
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapUIWsdlTemplate {

	private WsdlProject project;
	
	public SoapUIWsdlTemplate(WsdlProject project) {
		this.project = project;
	}

	public WsdlInfo wsdlInfo(String wsdlUrl) throws Exception {
		return new WsdlInfo(wsdlUrl);
	}
	
	public WsdlInterface[] importWsdl(String wsdlUrl, boolean createRequests) throws SoapUIException {
		// import amazon wsdl
		return WsdlInterfaceFactory.importWsdl(project, wsdlUrl, createRequests );
	}
	
	public Response invokeAt(String wsdlUrl, int index) throws SoapUIException, SubmitException {
		
		// import amazon wsdl
		WsdlInterface iface = this.importWsdl(wsdlUrl, true)[0];
		
		// get desired operation
		WsdlOperation operation = (WsdlOperation) iface.getOperationAt(index);
		
		// create a new empty request for that operation
		WsdlRequest request = operation.addNewRequest( "My request" );
		
		// generate the request content from the schema
		request.setRequestContent( operation.createRequest( true ) );
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);
		
		context.put("", "127.0.0.1");
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		return submit.getResponse();
		
	}

	public <T> T invokeAt(String wsdlUrl, int index, ResponseHandler<T> handler) throws SoapUIException, SubmitException {
		// wait for the response
		Response response = this.invokeAt(wsdlUrl, index);
		// handle response
		return handler.handleResponse(response);
	}
	
	public Response invokeByName(String wsdlUrl, String operationName) throws SoapUIException, SubmitException {
		
		// import amazon wsdl
		WsdlInterface iface = this.importWsdl(wsdlUrl, true)[0];
		
		WsdlContext wsdlContext	= iface.getDefinitionContext();
		
		// get desired operation
		WsdlOperation operation = (WsdlOperation) iface.getOperationByName( operationName );
		
		// create a new empty request for that operation
		WsdlRequest request = operation.addNewRequest( "My request" );
		
		// generate the request content from the schema
		request.setRequestContent( operation.createRequest( true ) );
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);
		
		context.put("", "127.0.0.1");
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		return submit.getResponse();
		
	}

	public <T> T invokeByName(String wsdlUrl, String operationName, ResponseHandler<T> handler) throws SoapUIException, SubmitException {
		// wait for the response
		Response response = this.invokeByName(wsdlUrl, operationName);
		// handle response
		return handler.handleResponse(response);
	}
	
}
