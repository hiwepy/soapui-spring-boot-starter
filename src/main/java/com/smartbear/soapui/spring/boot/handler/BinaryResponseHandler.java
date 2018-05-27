package com.smartbear.soapui.spring.boot.handler;

import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;

public class BinaryResponseHandler implements ResponseHandler<byte[]> {

	@Override
	public byte[] handleResponse(Response response) throws SoapUIException {
		// 响应内容
		return response.getRawResponseData();
	}

}
