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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.wsdl.WsdlInfo;
import com.smartbear.soapui.spring.boot.wsdl.WsdlInterfaceInfo;
import com.smartbear.soapui.spring.boot.wsdl.WsdlOperationInfo;

public class SoapuiWsdlTemplate_Test extends AbstractWsdlTemplate_Test {
	
	@Test
	public void getOperationAt() throws XmlException, IOException, SoapUIException, SubmitException {
		
		System.err.println( "getOperationAt===========================================");
		
		// wait for the response
		WsdlOperation operation = wsdlTemplate.getOperationAt(wsdlUrl, 0);
			
		System.out.println( "Name : " + operation.getName() );
		System.out.println( "Action : " + operation.getAction() );
		System.out.println( "Anonymous : " + operation.getAnonymous() );
		System.out.println( "Description : " + operation.getDescription() );
		System.out.println( "InputName : " + operation.getInputName() );
		System.out.println( "OutputName : " + operation.getOutputName() );
		System.out.println( "RequestCount : " + operation.getRequestCount() );
		
	}
	
	@Test
	public void getOperationByName() throws XmlException, IOException, SoapUIException, SubmitException {
		
		System.err.println( "getOperationByName===========================================");
		
		// wait for the response
		WsdlOperation operation = wsdlTemplate.getOperationByName(wsdlUrl, "getCountryCityByIp");
			
		System.out.println( "Name : " + operation.getName() );
		System.out.println( "Action : " + operation.getAction() );
		System.out.println( "Anonymous : " + operation.getAnonymous() );
		System.out.println( "Description : " + operation.getDescription() );
		System.out.println( "InputName : " + operation.getInputName() );
		System.out.println( "OutputName : " + operation.getOutputName() );
		System.out.println( "RequestCount : " + operation.getRequestCount() );
		
	}
	
	@Test
	public void getOperationList() throws XmlException, IOException, SoapUIException, SubmitException {
		
		System.err.println( "getOperationList===========================================");
		
		// wait for the response
		List<Operation> result = wsdlTemplate.getOperationList(wsdlUrl);
		for (Operation opt : result) {
			
			WsdlOperation operation = (WsdlOperation) opt;
			
			System.out.println( "Name : " + operation.getName() );
			System.out.println( "Action : " + operation.getAction() );
			System.out.println( "Anonymous : " + operation.getAnonymous() );
			System.out.println( "Description : " + operation.getDescription() );
			System.out.println( "InputName : " + operation.getInputName() );
			System.out.println( "OutputName : " + operation.getOutputName() );
			System.out.println( "RequestCount : " + operation.getRequestCount() );
			
		}
		
	}
	
	@Test
	public void getOperations() throws XmlException, IOException, SoapUIException, SubmitException {
		
		System.err.println( "getOperations===========================================");
		
		// wait for the response
		Map<String, Operation> result = wsdlTemplate.getOperations(wsdlUrl);
		Iterator<Entry<String, Operation>> ite = result.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Operation> entity = ite.next();
			
			System.out.println( "Name : " + entity.getKey() );
			
			WsdlOperation operation = (WsdlOperation) entity.getValue();
			
			System.out.println( "Action : " + operation.getAction() );
			System.out.println( "Anonymous : " + operation.getAnonymous() );
			System.out.println( "Description : " + operation.getDescription() );
			System.out.println( "InputName : " + operation.getInputName() );
			System.out.println( "OutputName : " + operation.getOutputName() );
			System.out.println( "RequestCount : " + operation.getRequestCount() );
			
		}
		
	}
	
	
	@Test
	public void getWsdlInfo() throws XmlException, IOException, SoapUIException, SubmitException {
		
		System.err.println( "getWsdlInfo===========================================");
		
		// wait for the response
		WsdlInfo info = wsdlTemplate.getWsdlInfo(wsdlUrl);
		
		System.out.println( "wsdlUrl : " + info.getWsdlUrl() );
		
		List<WsdlInterfaceInfo> result = info.getInterfaces();
		for (WsdlInterfaceInfo ifaceInfo : result) {
			
			System.out.println( "Adrress : " + ifaceInfo.getAdrress() );
			System.out.println( "InterfaceDesc : " + ifaceInfo.getInterfaceDesc() );
			System.out.println( "InterfaceName : " + ifaceInfo.getInterfaceName() );
			System.out.println( "InterfaceType : " + ifaceInfo.getInterfaceType() );
			System.out.println( "SoapVersion : " + ifaceInfo.getSoapVersion().getName() );
			
			List<WsdlOperationInfo> operations = ifaceInfo.getOperations();
			for (WsdlOperationInfo optInfo : operations) {
				
				
				
				System.out.println( "EndPoint : " + optInfo.getEndPoint());
				System.out.println( "OperationDesc : " + optInfo.getOperationDesc());
				System.out.println( "OperationName : " + optInfo.getOperationName());
				System.out.println( "RequestXml : " + optInfo.getRequestXml());
				System.out.println( "ResponseXml : " + optInfo.getResponseXml());
				System.out.println( "Action : " + optInfo.getSoapAction());
				System.out.println( "TargetNameSpace : " + optInfo.getTargetNameSpace());
				System.out.println( "TargetXsd : " + optInfo.getTargetXsd());
				System.out.println( "OperationType : " + optInfo.getOperationType().toString());
				System.out.println( "InputDesc : " + optInfo.getInputDesc());
				System.out.println( "InputNames : " + optInfo.getInputNames());
				System.out.println( "InputType : " + optInfo.getInputType());
				System.out.println( "OutputNames : " + optInfo.getOutputNames());
				System.out.println( "OutputType : " + optInfo.getOutputType());
				
			}
			
		}
		
	}

}
