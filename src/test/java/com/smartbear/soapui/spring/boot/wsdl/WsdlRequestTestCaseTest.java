/*
 * Copyright 2004-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
*/

package com.smartbear.soapui.spring.boot.wsdl;

import static org.junit.Assert.assertNotNull;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.iface.Response;

/**
 * SoapUI 官方示例
 */
public class WsdlRequestTestCaseTest {

	private WsdlProject project;
	
	@Rule  
    public ContiPerfRule i = new ContiPerfRule();  
	
	@Before
	public void setup() throws Exception {
		// create new project
       project = new WsdlProject();
       project.setCacheDefinitions(true);
	}
	
    @Test
    @PerfTest(invocations = 2, threads = 1)  
    //@Required(max = 1200, average = 250, totalTime = 60000)  
    public void testRequest() throws Exception {

        // import amazon wsdl
        WsdlInterface[] ifaces = WsdlInterfaceFactory.importWsdl(project, "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl",
                true);

        for (WsdlInterface iface : ifaces) {
		
	        // get "Help" operation
	        WsdlOperation operation = (WsdlOperation) iface.getOperationByName("getWeatherbyCityName");
	
	        // create a new empty request for that operation
	        WsdlRequest request = operation.addNewRequest("My request");
	
	        // generate the request content from the schema
	        request.setRequestContent(operation.createRequest(true));
	
	        // submit the request
	        WsdlSubmit submit = (WsdlSubmit) request.submit(new WsdlSubmitContext(request), false);
	
	        // wait for the response
	        Response response = submit.getResponse();
	
	        // print the response
	        String content = response.getContentAsString();
	        //System.out.println( content );
	        assertNotNull(content);
	        
	        System.err.println("=============================================================");
        }
    }
}