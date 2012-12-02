package com.fooding.contracts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

public interface WebApiContract<T, R> {	
	List<T> getlist() 
			throws ClientProtocolException, IOException;
	R get(T entity) 
			throws ClientProtocolException, IOException;
	R post(T entity) 
			throws NullPointerException, UnsupportedEncodingException, ClientProtocolException, IOException;
	R put(T entity) 
			throws NullPointerException, UnsupportedEncodingException, ClientProtocolException, IOException;
	R delete(T entity) 
			throws NullPointerException, ClientProtocolException, IOException;
}
