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
package com.smartbear.soapui.spring.boot.handler;

import java.io.ByteArrayInputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;


public class Httpclient3RequestHandler implements SoapRequestHandler {

	private HttpClient httpclient;
	private AbstractResponseHandler<String> handler;
	
	@Override
	public void preHande() {

		if (httpclient == null) {
			//创建忽略任何安全校验的httpClient实例.
			httpclient =  HttpClients.custom().build();
			handler = new PlainTextResponseHandler(HttpClientContext.create(), "UTF-8");
		}

	}

	@Override
	public String doHandle(String address, String action, String message) throws Exception {
		
		HttpPost httpPost  = new HttpPost(address); 
		
		String responseBodyAsString;
		try {
			httpPost.addHeader("SOAPAction", action);
			httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(message.getBytes("UTF-8"))));
			responseBodyAsString = httpclient.execute(httpPost, handler , handler.getContext());
		} finally {
			httpPost.releaseConnection();
		}

		return responseBodyAsString;
	}

}
