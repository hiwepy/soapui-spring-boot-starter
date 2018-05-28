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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class WsdlMethodInfo {
	
	private String methodName;
	private String methodDesc;
	private Map<String, Map> methodName2InputParam = new HashMap();
	private String targetNameSpace;
	private String soapAction;
	private String endPoint;
	private String targetXsd;
	private List<String> inputNames;
	private List<String> inputType;
	private List<String> inputDesc;
	private List<String> outputNames;
	private List<String> outputType;
	private String sep = "#";

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getInputNames() {
		return this.inputNames;
	}

	public void setInputNames(List<String> inputNames) {
		this.inputNames = inputNames;
	}

	public Map<String, Map> getMethodName2InputParam() {
		return this.methodName2InputParam;
	}

	public void setMethodName2InputParam(Map<String, Map> methodName2InputParam) {
		this.methodName2InputParam = methodName2InputParam;
	}

	public String getTargetNameSpace() {
		return this.targetNameSpace;
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
		return this.endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getTargetXsd() {
		return this.targetXsd;
	}

	public void setTargetXsd(String targetXsd) {
		this.targetXsd = targetXsd;
	}

	public List<String> getOutputNames() {
		return this.outputNames;
	}

	public void setOutputNames(List<String> outputNames) {
		this.outputNames = outputNames;
	}

	public List<String> getInputType() {
		return this.inputType;
	}

	public void setInputType(List<String> inputType) {
		this.inputType = inputType;
	}

	public List<String> getOutputType() {
		return this.outputType;
	}

	public void setOutputType(List<String> outputType) {
		this.outputType = outputType;
	}

	public List<String> getInputDesc() {
		return this.inputDesc;
	}

	public void setInputDesc(List<String> inputDesc) {
		this.inputDesc = inputDesc;
	}

	public String madeNewString() {
		StringBuffer su = new StringBuffer();
		su.append(this.methodName);
		su.append(this.sep);
		su.append(this.inputType == null ? "" : StringUtils.join(this.inputType.toArray(), "@"));
		su.append(this.sep);
		su.append(this.inputNames == null ? "" : StringUtils.join(this.inputNames.toArray(), "@"));
		su.append(this.sep);
		su.append(this.methodDesc == null ? "" : this.methodDesc);
		su.append(this.sep);
		su.append(this.sep);
		su.append(this.soapAction == null ? "" : this.soapAction);
		su.append(this.sep);
		su.append(this.outputType == null ? "" : StringUtils.join(this.outputType.toArray(), "@"));
		su.append(this.sep);
		su.append(this.targetXsd == null ? "" : this.targetXsd);
		return su.toString();
	}

	public String getMethodDesc() {
		return this.methodDesc;
	}

	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
}