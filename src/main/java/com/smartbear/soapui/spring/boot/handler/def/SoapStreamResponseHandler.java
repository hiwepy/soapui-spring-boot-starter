package com.smartbear.soapui.spring.boot.handler.def;

import java.io.ByteArrayInputStream;

import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.smartbear.soapui.spring.boot.handler.SoapResponseHandler;

/**
 * 请求响应处理：返回ByteArrayInputStream对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class SoapStreamResponseHandler implements SoapResponseHandler<ByteArrayInputStream> {

	@Override
	public ByteArrayInputStream handleResponse(WsdlOperation operationInst, Response response) throws SoapUIException {
		// 响应内容
		return new ByteArrayInputStream(response.getRawResponseData());
	}

}
