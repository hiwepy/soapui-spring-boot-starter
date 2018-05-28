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

import java.util.ArrayList;
import java.util.List;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;

/**
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
public class WsdlInterfaceInfo {
	
	private String[] adrress;
	private String interfaceName;
	private String interfaceType;
	private String interfaceDesc;
	private SoapVersion soapVersion;
	private List<WsdlOperationInfo> operations;


	public WsdlInterfaceInfo(WsdlInterface wsdlInterface) {
		this.interfaceName = wsdlInterface.getName();
		this.interfaceType = wsdlInterface.getInterfaceType();
		this.interfaceDesc = wsdlInterface.getDescription();
		this.soapVersion = wsdlInterface.getSoapVersion();
		this.adrress = wsdlInterface.getEndpoints();

		int operationNum = wsdlInterface.getOperationCount();
		List<WsdlOperationInfo> operations = new ArrayList<WsdlOperationInfo>();

		for (int i = 0; i < operationNum; i++) {
			WsdlOperation operation = (WsdlOperation) wsdlInterface.getOperationAt(i);
			WsdlOperationInfo operationInfo = new WsdlOperationInfo(operation);
			operations.add(operationInfo);
		}

		this.operations = operations;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public List<WsdlOperationInfo> getOperations() {
		return operations;
	}

	public void setOperations(List<WsdlOperationInfo> operations) {
		this.operations = operations;
	}

	public String[] getAdrress() {
		return adrress;
	}

	public void setAdrress(String[] adrress) {
		this.adrress = adrress;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getInterfaceDesc() {
		return interfaceDesc;
	}

	public void setInterfaceDesc(String interfaceDesc) {
		this.interfaceDesc = interfaceDesc;
	}

	public SoapVersion getSoapVersion() {
		return soapVersion;
	}

	public void setSoapVersion(SoapVersion soapVersion) {
		this.soapVersion = soapVersion;
	}
	
}