package com.smartbear.soapui.spring.boot.handler;

import com.alibaba.fastjson.JSONObject;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;

/**
 * 请求响应处理：返回JSONObject对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class JSONResponseHandler implements ResponseHandler<JSONObject> {
	
	@Override
	public JSONObject handleResponse(Response response) throws SoapUIException {
		// 响应内容
		return JSONObject.parseObject(response.getContentAsString());
	}
}
