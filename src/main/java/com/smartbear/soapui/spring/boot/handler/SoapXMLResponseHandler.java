 package com.smartbear.soapui.spring.boot.handler;

import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;

/**
 * 请求响应处理：返回String对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapXMLResponseHandler implements SoapResponseHandler<String> {
 
	@Override
	public String handleResponse(Response response) throws SoapUIException {
		// 响应内容
		return response.getContentAsXml();
	}
}

 
