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

import com.eviware.soapui.impl.wsdl.WsdlOperation;

/**
 * 
 * OperationInfo.java Create on 2016年7月20日 下午9:03:42
 * 
 * 类功能说明: 方法信息
 *
 * Copyright: Copyright(c) 2013 Company: COSHAHO
 * 
 * @Version 1.0
 * @Author 何科序
 */
public class OperationInfo {
	
	private String operationName;

	private String requestXml;

	private String responseXml;

	public OperationInfo(WsdlOperation operation) {
		operationName = operation.getName();
		requestXml = operation.createRequest(true);
		responseXml = operation.createResponse(true);
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public String getResponseXml() {
		return responseXml;
	}

	public void setResponseXml(String responseXml) {
		this.responseXml = responseXml;
	}
}