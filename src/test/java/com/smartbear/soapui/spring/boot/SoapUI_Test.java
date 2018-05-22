package com.smartbear.soapui.spring.boot;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.rest.RestRequest;
import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.support.AbstractInterface;
import com.eviware.soapui.impl.wsdl.InterfaceFactoryRegistry;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.environment.EnvironmentListener;
import com.eviware.soapui.model.environment.Property;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.support.ProjectListenerAdapter;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.tools.SoapUIMockServiceRunner;
import com.eviware.soapui.tools.SoapUITestCaseRunner;

public class SoapUI_Test {

	/*@Test
    public void testCalculatorService() throws Exception {
        SoapUITestCaseRunner testCaseRunner = new SoapUITestCaseRunner();
        SoapUIMockServiceRunner mockServiceRunner = new SoapUIMockServiceRunner();

        testCaseRunner.setProjectFile("src/test/resources/calculator-soapui-project.xml");
        mockServiceRunner.setProjectFile("src/test/resources/calculator-soapui-project.xml");
        mockServiceRunner.run();
        testCaseRunner.run();
	}*/
	 
	//@Test
	public void testSoap() throws XmlException, IOException, SoapUIException, SubmitException {
		
		// create new project
		WsdlProject project = new WsdlProject();
		
		project.addEnvironmentListener(new EnvironmentListener() {
			
			@Override
			public void propertyValueChanged(Property property) {
				System.out.println(property.getName() + ":" + property.getValue());
			}
		});
		project.addProjectListener(new ProjectListenerAdapter() {
			
			@Override
			public void afterLoad(Project project) {
				super.afterLoad(project);
			}
			
		});
		
		// import amazon wsdl
		WsdlInterface iface = WsdlInterfaceFactory.importWsdl( 
		 project, "http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl", true )[0];

		// get desired operation
		WsdlOperation operation = (WsdlOperation) iface.getOperationByName( "getCountryCityByIp" );
		
		// create a new empty request for that operation
		WsdlRequest request = operation.addNewRequest( "My request" );
		
		// generate the request content from the schema
		request.setRequestContent( operation.createRequest( true ) );
		
		WsdlSubmitContext context = new WsdlSubmitContext(request);
		
		context.put("", "127.0.0.1");
		
		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit( context, false );

		// wait for the response
		Response response = submit.getResponse();
		
		// print the response
		String content = response.getContentAsString();
		System.out.println( content );
		assertNotNull( content );
		//assertTrue( content.indexOf( "404 Not Found" ) > 0  );
		
	}
	 
	@Test
	public void testRest() throws XmlException, IOException, SoapUIException, SubmitException {
		
		// create new project
		WsdlProject project = new WsdlProject();
		project.addEnvironmentListener(new EnvironmentListener() {
			
			@Override
			public void propertyValueChanged(Property property) {
				System.out.println(property.getName() + ":" + property.getValue());
			}
		});
		project.addProjectListener(new ProjectListenerAdapter() {
			
			@Override
			public void afterLoad(Project project) {
				super.afterLoad(project);
			}
			
		});
		
		RestService rest = (RestService) InterfaceFactoryRegistry.createNew(project, RestServiceFactory.REST_TYPE, "test");
		
		
		RestResource res = rest.getResourcesByFullPath("https://www.sojson.com/open/api/weather/json.shtml")[0];
		 
		res.getChildResourceList();
		
		 /*
		// wait for the response
		Response response = submit.getResponse();
		
		// print the response
		String content = response.getContentAsString();
		System.out.println( content );
		assertNotNull( content );
		assertTrue( content.indexOf( "404 Not Found" ) > 0  );*/
		
	}
	
	
}
