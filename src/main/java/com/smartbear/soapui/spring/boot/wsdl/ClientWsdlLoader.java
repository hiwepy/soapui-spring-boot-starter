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
package com.smartbear.soapui.spring.boot.wsdl;

import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class ClientWsdlLoader extends WsdlLoader {
	
	private static Logger logger = Logger.getLogger(ClientWsdlLoader.class);

	private boolean isAborted = false;
	private HttpClient httpClient;

	public ClientWsdlLoader(String url, HttpClient httpClient) {
		super(url);
		this.httpClient = httpClient;
	}

	@Override
	public InputStream load(String url) throws Exception {
		
		if (url.startsWith("file")) {
			return new URL(url).openStream();
		}

		GetMethod httpGetMethod = new GetMethod(url);
		httpGetMethod.setDoAuthentication(true);
		try {
			int result = this.httpClient.executeMethod(httpGetMethod);

			if (result != 200) {
				if ((result < 200) || (result > 299)) {
					throw new HttpException(
							"Received status code '" + result + "' on WSDL HTTP (GET) request: '" + url + "'.");
				}
				logger.warn("Received status code '" + result + "' on WSDL HTTP (GET) request: '" + url + "'.");
			}

			return new ByteArrayInputStream(httpGetMethod.getResponseBody());
		} finally {
			httpGetMethod.releaseConnection();
		}
	}

	public boolean abort() {
		this.isAborted = true;
		return true;
	}

	public boolean isAborted() {
		return this.isAborted;
	}

	public void close() {
	}
	
}