package com.smartbear.soapui.spring.boot;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.wsdl.InterfaceFactoryRegistry;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.EnvironmentListener;
import com.eviware.soapui.model.environment.Property;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.support.ProjectListenerAdapter;
import com.eviware.soapui.support.SoapUIException;

public class SoapUI_Rest_Test {

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
