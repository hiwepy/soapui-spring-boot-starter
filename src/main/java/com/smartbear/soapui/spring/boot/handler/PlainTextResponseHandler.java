package com.smartbear.soapui.spring.boot.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

/**
 * http请求响应处理：返回字符串结果对象
 */
public class PlainTextResponseHandler extends AbstractResponseHandler<String> {

	public PlainTextResponseHandler(String charset) {
		super(HttpClientContext.create(), charset);
	}
	
	public PlainTextResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		
		StatusLine statusLine = response.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				ContentType contentType = ContentType.getOrDefault(entity);
				String charset = null != contentType.getCharset() ? null : contentType.getCharset().name();
				if (charset == null) {
					charset = getCharset();
				}
				// 响应内容
				return EntityUtils.toString(entity, charset);
			} finally {
				// 销毁
				EntityUtils.consume(entity);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
