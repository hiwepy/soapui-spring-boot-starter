package com.smartbear.soapui.spring.boot;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.schema.SchemaTypeImpl;
import org.junit.Test;

import com.eviware.soapui.config.RestServiceConfig;
import com.eviware.soapui.config.impl.RestServiceConfigImpl;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;

public class SoapUI_Test {

	@Test
	public void testSoap() throws XmlException, IOException, SoapUIException, SubmitException {
		
		// create new project
		WsdlProject project = new WsdlProject();

		// import amazon wsdl
		WsdlInterface iface = WsdlInterfaceFactory.importWsdl( 
		 project, "http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl", true )[0];

		// get desired operation
		WsdlOperation operation = (WsdlOperation) iface.getOperationByName( "getCountryCityByIp" );
		
		// create a new empty request for that operation
		WsdlRequest request = operation.addNewRequest( "My request" );

		// generate the request content from the schema
		request.setRequestContent( operation.createRequest( true ) );
		
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( new WsdlSubmitContext(request), false );

		// wait for the response
		Response response = submit.getResponse();
		
		// print the response
		String content = response.getContentAsString();
		System.out.println( content );
		assertNotNull( content );
		assertTrue( content.indexOf( "404 Not Found" ) > 0  );
		
	}
	
	@Test
	public void testRest() throws XmlException, IOException, SoapUIException, SubmitException {
		
		// create new project
		WsdlProject project = new WsdlProject();
		
		RestService rest = new RestServiceFactory().createNew(project, "");
		
		// import amazon wsdl
		WsdlInterface iface = WsdlInterfaceFactory.importWsdl( 
		 project, "http://www.mycorp.com/somewsdl.wsdl", true )[0];

		// get desired operation
		WsdlOperation operation = (WsdlOperation) iface.getOperationByName( "MyOperation" );

		// create a new empty request for that operation
		WsdlRequest request = operation.addNewRequest( "My request" );

		// generate the request content from the schema
		request.setRequestContent( operation.createRequest( true ) );
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( new WsdlSubmitContext(request), false );

		// wait for the response
		Response response = submit.getResponse();
		
		// print the response
		String content = response.getContentAsString();
		System.out.println( content );
		assertNotNull( content );
		assertTrue( content.indexOf( "404 Not Found" ) > 0  );
		
	}
	
	
}
