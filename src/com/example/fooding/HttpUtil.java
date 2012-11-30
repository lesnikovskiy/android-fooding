package com.example.fooding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public final class HttpUtil {	
	final static private String COOKIE_HEADER = "Cookie";
	final static private String HD_COOKIE = "foodingaccess=1-b8767f0228e22a1d3fe10e12e6d3d656";
		
	public static String get(String url) 
			throws ClientProtocolException, IOException  {    	
    	HttpClient client = new DefaultHttpClient();
    	
    	CookieStore cookieStore = new BasicCookieStore();
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpRequestBase httpget = new HttpGet(url);
		httpget.addHeader(COOKIE_HEADER, HD_COOKIE);
		HttpResponse httpResponse = client.execute(httpget);
		
		String line = "";
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(httpResponse.getEntity().getContent()));
		
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		reader.close();
		
		return sb.toString().trim().replace("\n", "").replace("\r", "");
	}
		
	
	public static String post(String url, List<NameValuePair> pairs) 
			throws UnsupportedEncodingException, ClientProtocolException, IOException {		
		HttpClient httpClient = new DefaultHttpClient();
		
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    	
		
		HttpPost httppost = new HttpPost(url);	
		httppost.setHeader(COOKIE_HEADER, HD_COOKIE);
		httppost.setEntity(new UrlEncodedFormEntity(pairs));
		
		HttpResponse response = httpClient.execute(httppost);
		
		String line = "";
		StringBuilder result = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		
		reader.close();
		
		return result.toString().trim().replace("\n", "").replace("\r", "");
	}
	
	public static String put(String url, List<NameValuePair> pairs) 
			throws UnsupportedEncodingException, ClientProtocolException, IOException {		
		HttpClient httpClient = new DefaultHttpClient();
		
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    	
		
		HttpPut httpput = new HttpPut(url);	
		httpput.setHeader(COOKIE_HEADER, HD_COOKIE);
		httpput.setEntity(new UrlEncodedFormEntity(pairs));
		
		HttpResponse response = httpClient.execute(httpput);
		
		String line = "";
		StringBuilder result = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		
		reader.close();
		
		return result.toString().trim().replace("\n", "").replace("\r", "");
	}
	
	public static String delete(String url) 
			throws UnsupportedEncodingException, ClientProtocolException, IOException {		
		HttpClient client = new DefaultHttpClient();
    	
    	CookieStore cookieStore = new BasicCookieStore();
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpDelete httpdelete = new HttpDelete(url);
		httpdelete.addHeader(COOKIE_HEADER, HD_COOKIE);
		HttpResponse httpResponse = client.execute(httpdelete);
		
		String line = "";
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(httpResponse.getEntity().getContent()));
		
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		reader.close();
		
		return sb.toString().trim().replace("\n", "").replace("\r", "");
	}
}
