 package com.smartbear.soapui.spring.boot.handler.def;

import java.util.Map;

import org.apache.xmlbeans.XmlException;

import com.eviware.soapui.impl.wsdl.support.soap.SoapUtils;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.handler.SoapResponseHandler;
import com.smartbear.soapui.spring.boot.utils.SoapuiResponseUtils;

/**
 * 请求响应处理：返回String对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapMapResponseHandler implements SoapResponseHandler<Map<String, String>> {
 
	@Override
	public Map<String, String> handleResponse(Response response) throws SoapUIException {
		// 响应内容
		String responseContent = response.getContentAsString();
		try {
			if(SoapUtils.isSoapFault(responseContent)) {
				
			}
		} catch (XmlException e) {
			e.printStackTrace();
		}
		
		return SoapuiResponseUtils.populateResponseMap(responseContent);
	}
	
}

 
