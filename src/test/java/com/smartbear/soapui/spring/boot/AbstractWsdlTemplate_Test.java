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

import org.apache.xmlbeans.XmlException;
import org.junit.Before;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.EnvironmentListener;
import com.eviware.soapui.model.environment.Property;
import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.support.ProjectListenerAdapter;
import com.eviware.soapui.support.SoapUIException;

public abstract class AbstractWsdlTemplate_Test {

	protected SoapuiWsdlTemplate wsdlTemplate;
	protected String wsdlUrl = "http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl";
	
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
		
		wsdlTemplate = new SoapuiWsdlTemplate(project);
		
	}
	
	
}
