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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;

/**
 * SoapUI 官方示例
 */
public class WsdlProjectTestCaseTest {

    @Test
    public void testComplexLoad() throws Exception {
       
        WsdlProject project = new WsdlProject();
        WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, "http://localhost:80/wsdls/test8/TestService.wsdl");

        assertEquals(1, wsdls.length);
    }

    @Test
    public void testClasspathLoad() throws Exception {
        String str = SoapUI.class.getResource("/soapui-projects/sample-soapui-project.xml").toURI().toString();

        assertNotNull(new WsdlProject(str));
    }

    public void testInit() throws Exception {
        assertTrue(new WsdlProject().isCacheDefinitions());
    }
}
