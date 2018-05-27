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
import com.eviware.soapui.impl.wsdl.WsdlOperation;

/**
 * 
 * InterfaceInfo.java Create on 2016年7月20日 下午9:03:21    
 *    
 * 类功能说明: 接口信息
 *
 * Copyright: Copyright(c) 2013 
 * Company: COSHAHO
 * @Version 1.0
 * @Author 何科序
 */
public class InterfaceInfo 
{
    private String interfaceName;
    
    private List<OperationInfo> operations;
    
    private String[] adrress;
    
    public InterfaceInfo(WsdlInterface wsdlInterface)
    {
        this.interfaceName = wsdlInterface.getName();
        
        this.adrress = wsdlInterface.getEndpoints();
        
        int operationNum = wsdlInterface.getOperationCount();
        List<OperationInfo> operations = new ArrayList<OperationInfo>();
        
        for(int i = 0; i < operationNum; i++)
        {
            WsdlOperation operation = ( WsdlOperation )wsdlInterface.getOperationAt( i );
            OperationInfo operationInfo = new OperationInfo(operation);
            operations.add(operationInfo);
        }
        
        this.operations = operations;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<OperationInfo> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationInfo> operations) {
        this.operations = operations;
    }
    
    public String[] getAdrress() {
        return adrress;
    }

    public void setAdrress(String[] adrress) {
        this.adrress = adrress;
    }
}