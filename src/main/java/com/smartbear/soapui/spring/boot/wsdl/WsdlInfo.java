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
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.support.SoapUIException;

/**
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class WsdlInfo {

	private String wsdlUrl;

	private List<WsdlInterfaceInfo> interfaces;

	public WsdlInfo(String wsdlUrl) throws SoapUIException {
		try {
			this.wsdlUrl = wsdlUrl;
			// create new project
			WsdlProject project = new WsdlProject();
			WsdlInterface[] wsdlInterfaces = WsdlImporter.importWsdl(project, wsdlUrl);
			if (null != wsdlInterfaces) {
				List<WsdlInterfaceInfo> interfaces = new ArrayList<WsdlInterfaceInfo>();
				for (WsdlInterface wsdlInterface : wsdlInterfaces) {
					WsdlInterfaceInfo interfaceInfo = new WsdlInterfaceInfo(wsdlInterface);
					interfaces.add(interfaceInfo);
				}
				this.interfaces = interfaces;
			}
		} catch (Exception e) {
			throw new SoapUIException("Failed to import WSDL '" + wsdlUrl + "'.", e);
		}
	}

	public WsdlInfo(String wsdlUrl, WsdlProject project) throws SoapUIException {
		try {
			this.wsdlUrl = wsdlUrl;
			WsdlInterface[] wsdlInterfaces = WsdlImporter.importWsdl(project, wsdlUrl);
			if (null != wsdlInterfaces) {
				List<WsdlInterfaceInfo> interfaces = new ArrayList<WsdlInterfaceInfo>();
				for (WsdlInterface wsdlInterface : wsdlInterfaces) {
					WsdlInterfaceInfo interfaceInfo = new WsdlInterfaceInfo(wsdlInterface);
					interfaces.add(interfaceInfo);
				}
				this.interfaces = interfaces;
			}
		} catch (Exception e) {
			throw new SoapUIException("Failed to import WSDL '" + wsdlUrl + "'.", e);
		}
	}
	
	/**
	 * @param wsdlUrl wsdl地址
	 * @throws Exception
	 */
	public WsdlInfo(String wsdlUrl, WsdlInterface[] wsdlInterfaces) throws SoapUIException {
		this.wsdlUrl = wsdlUrl;
		if (null != wsdlInterfaces) {
			List<WsdlInterfaceInfo> interfaces = new ArrayList<WsdlInterfaceInfo>();
			for (WsdlInterface wsdlInterface : wsdlInterfaces) {
				WsdlInterfaceInfo interfaceInfo = new WsdlInterfaceInfo(wsdlInterface);
				interfaces.add(interfaceInfo);
			}
			this.interfaces = interfaces;
		}
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public List<WsdlInterfaceInfo> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<WsdlInterfaceInfo> interfaces) {
		this.interfaces = interfaces;
	}
}