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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.util.Lists;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.support.wsdl.UrlWsdlLoader;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.smartbear.soapui.spring.boot.wsdl.WsdlInfo;

/**
 * https://www.soapui.org/developers-corner/integrating-with-soapui.html
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapuiWsdlTemplate {

	private WsdlProject project;
	private boolean createRequests = true;
	private LoadingCache<String, WsdlInterface[]> caches;
	
	public SoapuiWsdlTemplate(WsdlProject project, SoapuiProperties properties) {
		this.project = project;
		this.createRequests = properties.isCreateRequests();
		this.caches = CacheBuilder.newBuilder()
				.maximumSize(properties.getMaximumCacheSize())
				.refreshAfterWrite(properties.getCacheDuration(), TimeUnit.MINUTES)
				.build(new CacheLoader<String, WsdlInterface[]>() {
					@Override
					public WsdlInterface[] load(String wsdlUrl) throws Exception {
						//WsdlInterface[] wsdlInterfaces = WsdlImporter.importWsdl(project, wsdlUrl);
						
						UrlWsdlLoader loader = new UrlWsdlLoader(wsdlUrl, project);
						return WsdlInterfaceFactory.importWsdl(project, wsdlUrl, createRequests, loader);
					}
				});
	}

	public WsdlInfo getWsdlInfo(String wsdlUrl) throws SoapUIException {
		return new WsdlInfo(wsdlUrl, this.getWsdlInterfaces(wsdlUrl));
	}
	
	public WsdlInterface[] getWsdlInterfaces(String wsdlUrl) throws SoapUIException {
		try {
			return caches.get(wsdlUrl);
		} catch (Exception e) {
			try {
				// import wsdl
				return WsdlImporter.importWsdl(project, wsdlUrl);
			} catch (Exception cause) {
				throw new SoapUIException("Failed to import WSDL '" + wsdlUrl + "'.", cause);
			}
		}
	}
	
	public Map<String, Operation> getOperations(String wsdlUrl) throws SoapUIException {
		Map<String, Operation> result = new HashMap<String, Operation>();
		WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdlUrl);
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
			result.putAll(wsdlInterface.getOperations());
		}
		return result;
	}
	
	public List<Operation> getOperationList(String wsdlUrl) throws SoapUIException {
		List<Operation> operationList = Lists.newArrayList();
		WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdlUrl);
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
			operationList.addAll(wsdlInterface.getOperationList());
		}
		return operationList;
	}
	
	public WsdlOperation getOperationAt(String wsdlUrl, int index) throws SoapUIException {
		WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdlUrl);
		Operation operationInst;
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
			operationInst = wsdlInterface.getOperationAt(index);
			if (operationInst != null) {
				return (WsdlOperation) operationInst;
			}
		}
		throw new UnsupportedOperationException("Operation not found by WSDL '" + wsdlUrl + "'.");
	}

	public WsdlOperation getOperationByName(String wsdlUrl, String operationName) throws SoapUIException {
		WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdlUrl);
		Operation operationInst;
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
			operationInst = wsdlInterface.getOperationByName(operationName);
			if (operationInst != null) {
				return (WsdlOperation) operationInst;
			}
		}
		throw new UnsupportedOperationException(
				"Operation '" + operationName + "' not supported by WSDL '" + wsdlUrl + "'.");
	}
	
	public Response invokeAt(String wsdlUrl, int index) throws SoapUIException, SubmitException {
		
		// get desired operation
		WsdlOperation operation = this.getOperationAt(wsdlUrl ,index);
		
		// create a new empty request for that operation
		String requestName = "Request" + DigestUtils.md5Hex(wsdlUrl + "$operation-" + index);
		WsdlRequest request = operation.getRequestByName(requestName);
		if(null == request) {
			request = operation.addNewRequest(requestName);
		}
		
		// generate the request content from the schema
		String requestXML = operation.createRequest( true );
		
		// 对 requestContent 进行动态补全
		
		request.setRequestContent( requestXML );
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		return submit.getResponse();
		
	}
		
	public Response invokeByName(String wsdlUrl, String operationName) throws SoapUIException, SubmitException {
		
		// get desired operation
		WsdlOperation operation = this.getOperationByName(wsdlUrl, operationName);
		
		// create a new empty request for that operation
		String requestName = "Request" + DigestUtils.md5Hex(wsdlUrl + "$" + operationName);
		WsdlRequest request = operation.getRequestByName(requestName);
		if(null == request) {
			request = operation.addNewRequest(requestName);
		}
		
		//MessageExchange exchange = new WsdlResponseMessageExchange(request);
        //MessageExchange exchange = new RestResponseMessageExchange(request);
        //MessageExchange exchange = new HttpResponseMessageExchange(request);
        
		// generate the request content from the schema
		String requestXML = operation.createRequest( true );
		
		// 对 requestContent 进行动态补全
		
		request.setRequestContent( requestXML );
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		return submit.getResponse();
		
	}
	
}
