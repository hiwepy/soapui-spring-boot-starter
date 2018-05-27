package com.smartbear.soapui.spring.boot.handler;

import java.io.ByteArrayInputStream;

import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;

/**
 * 请求响应处理：返回ByteArrayInputStream对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class StreamResponseHandler implements ResponseHandler<ByteArrayInputStream> {

	@Override
	public ByteArrayInputStream handleResponse(Response response) throws SoapUIException {
		// 响应内容
		return new ByteArrayInputStream(response.getRawResponseData());
	}

}