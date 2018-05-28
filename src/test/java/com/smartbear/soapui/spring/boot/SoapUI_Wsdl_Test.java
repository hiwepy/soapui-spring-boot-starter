package com.smartbear.soapui.spring.boot;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;

import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.wsdl.InterfaceFactoryRegistry;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.EnvironmentListener;
import com.eviware.soapui.model.environment.Property;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.support.ProjectListenerAdapter;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.handler.SoapXMLResponseHandler;

public class SoapUI_Wsdl_Test {

	/*@Test
    public void testCalculatorService() throws Exception {
        SoapUITestCaseRunner testCaseRunner = new SoapUITestCaseRunner();
        SoapUIMockServiceRunner mockServiceRunner = new SoapUIMockServiceRunner();

        testCaseRunner.setProjectFile("src/test/resources/calculator-soapui-project.xml");
        mockServiceRunner.setProjectFile("src/test/resources/calculator-soapui-project.xml");
        mockServiceRunner.run();
        testCaseRunner.run();
	}*/
	
	private SoapuiWsdlTemplate template;
	private String wsdlUrl = "http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl";
	private SoapXMLResponseHandler handler = new SoapXMLResponseHandler();
	
	@Before
	public void setup() throws XmlException, IOException, SoapUIException {
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
		
		template = new SoapuiWsdlTemplate(project);
		
	}
	 
	@Test
	public void testSoap1() throws XmlException, IOException, SoapUIException, SubmitException {
		
		// wait for the response
		Response response = template.invokeAt(wsdlUrl, 0);
		
		// print the response
		String content = response.getContentAsString();
		System.out.println( content );
		assertNotNull( content );
		//assertTrue( content.indexOf( "404 Not Found" ) > 0  );
		
	}
	
	//@Test
	public void testSoap2() throws XmlException, IOException, SoapUIException, SubmitException {
		
		// wait for the response
		String content = template.invokeAt(wsdlUrl, 0, handler);
		// print the response
		System.out.println( content );
		assertNotNull( content );
		
	}
	
}
