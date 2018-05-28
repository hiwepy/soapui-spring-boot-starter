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

import java.util.List;

import javax.wsdl.OperationType;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.support.Constants;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.google.common.collect.Lists;
import com.smartbear.soapui.spring.boot.utils.XmlUtil;

/**
 * Wsdl Operation Info
 * 
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
public class WsdlOperationInfo {

	private String endPoint;
	private String operationName;
	private OperationType operationType;
	private String operationDesc;
	private String requestXml;
	private String responseXml;
	private String soapAction;
	private SoapVersion soapVersion;
	private String targetNameSpace;
	private String targetXsd;
	private List<String> inputNames;
	private List<String> inputType;
	private List<String> inputDesc;
	private List<String> outputNames;
	private List<String> outputType;
	private String sep = "#";

	public WsdlOperationInfo(WsdlOperation operation) {

		WsdlInterface wsdlInterface = operation.getInterface();

		this.operationName = operation.getName();
		this.operationDesc = operation.getDescription();
		this.operationType = operation.getOperationType();

		this.requestXml = operation.createRequest(true);
		this.soapVersion = wsdlInterface.getSoapVersion();
		this.soapAction = this.soapVersion.getSoapActionHeader(operation.getAction());

		// 处理targetNameSpace
		this.targetNameSpace = requestXml.substring(requestXml.lastIndexOf("\"http://") + 1,
				requestXml.lastIndexOf("\">"));

		if (this.soapVersion.getEnvelopeNamespace().startsWith(Constants.SOAP11_ENVELOPE_NS)) {
			this.setTargetXsd("11");
		} else if (this.soapVersion.getEnvelopeNamespace().startsWith(Constants.SOAP12_ENVELOPE_NS)) {
			this.setTargetXsd("12");
		}

		this.inputType = Lists.newArrayList();
		this.inputNames = Lists.newArrayList();
		this.outputType = Lists.newArrayList();

		try {
			XmlUtil.parseXML(this.requestXml, this.inputType, this.inputNames, this.outputType);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		this.responseXml = operation.createResponse(true);
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}

	public String getTargetNameSpace() {
		return targetNameSpace;
	}

	public void setTargetNameSpace(String targetNameSpace) {
		this.targetNameSpace = targetNameSpace;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getTargetXsd() {
		return targetXsd;
	}

	public void setTargetXsd(String targetXsd) {
		this.targetXsd = targetXsd;
	}

	public List<String> getInputNames() {
		return inputNames;
	}

	public void setInputNames(List<String> inputNames) {
		this.inputNames = inputNames;
	}

	public List<String> getInputType() {
		return inputType;
	}

	public void setInputType(List<String> inputType) {
		this.inputType = inputType;
	}

	public List<String> getInputDesc() {
		return inputDesc;
	}

	public void setInputDesc(List<String> inputDesc) {
		this.inputDesc = inputDesc;
	}

	public List<String> getOutputNames() {
		return outputNames;
	}

	public void setOutputNames(List<String> outputNames) {
		this.outputNames = outputNames;
	}

	public List<String> getOutputType() {
		return outputType;
	}

	public void setOutputType(List<String> outputType) {
		this.outputType = outputType;
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
	
	@Override
	public String toString() {
		StringBuffer su = new StringBuffer();
		su.append(this.operationName);
		su.append(this.sep);
		su.append(this.inputType == null ? "" : StringUtils.join(this.inputType.toArray(), "@"));
		su.append(this.sep);
		su.append(this.inputNames == null ? "" : StringUtils.join(this.inputNames.toArray(), "@"));
		su.append(this.sep);
		su.append(this.operationDesc == null ? "" : this.operationDesc);
		su.append(this.sep);
		su.append(this.sep);
		su.append(this.soapAction == null ? "" : this.soapAction);
		su.append(this.sep);
		su.append(this.outputType == null ? "" : StringUtils.join(this.outputType.toArray(), "@"));
		su.append(this.sep);
		su.append(this.targetXsd == null ? "" : this.targetXsd);
		return su.toString();
	}
	
}