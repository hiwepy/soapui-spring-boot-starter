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

import java.util.ArrayList;
import java.util.List;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;

/**    
 * WsdlInfo.java Create on 2013-5-4 下午12:56:14    
 *    
 * 类功能说明:  wsdl解析入口
 *
 * Copyright: Copyright(c) 2013 
 * Company: COSHAHO
 * @Version 1.0
 * @Author 何科序   
 */
public class WsdlInfo 
{
    private String wsdlName;
    
    private List<InterfaceInfo> interfaces;
    
    /**
     * coshaho
     * @param path  wsdl地址
     * @throws Exception
     */
    public WsdlInfo(String path) throws Exception
    {
        WsdlProject project = new WsdlProject();
        WsdlInterface[] wsdlInterfaces = WsdlImporter.importWsdl( project, path );
        this.wsdlName = path;
        if(null != wsdlInterfaces)
        {    
            List<InterfaceInfo> interfaces = new ArrayList<InterfaceInfo>();
            for(WsdlInterface wsdlInterface : wsdlInterfaces)
            {
                InterfaceInfo interfaceInfo = new InterfaceInfo(wsdlInterface);
                interfaces.add(interfaceInfo);
            }
            this.interfaces = interfaces;
        }
    }

    public String getWsdlName() {
        return wsdlName;
    }

    public void setWsdlName(String wsdlName) {
        this.wsdlName = wsdlName;
    }

    public List<InterfaceInfo> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<InterfaceInfo> interfaces) {
        this.interfaces = interfaces;
    }
}