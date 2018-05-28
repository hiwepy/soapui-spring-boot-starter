package com.smartbear.soapui.spring.boot.handler;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.HttpClientContext;

public abstract class AbstractResponseHandler<T> implements ResponseHandler<T> {

	protected HttpClientContext context;
	protected String charsetStr;

	public AbstractResponseHandler(HttpClientContext context, String charset) {
		this.context = context;
		this.charsetStr = charset;
	}

	public HttpClientContext getContext() {

		return context;
	}

	public void setContext(HttpClientContext context) {

		this.context = context;
	}

	public String getCharset() {

		return charsetStr;
	}

	public void setCharset(String charset) {

		this.charsetStr = charset;
	}

}
