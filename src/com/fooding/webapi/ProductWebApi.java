package com.fooding.webapi;

import static com.fooding.utils.Constants.API_LIST_URL;
import static com.fooding.utils.Constants.API_PRODUCT_URL;
import static com.fooding.utils.Constants.HOST;
import static com.fooding.utils.Constants.ID;
import static com.fooding.utils.Constants.NAME;
import static com.fooding.utils.Constants.PRICE;
import static com.fooding.utils.Constants.REV;
import static com.fooding.utils.Constants.buildDeleteUrl;
import static com.fooding.utils.Constants.buildEntityUrl;
import static com.fooding.utils.Constants.buildListUrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;
import android.util.Log;

import com.fooding.contracts.WebApiContract;
import com.fooding.entities.Product;
import com.fooding.entities.ProductSet;
import com.fooding.utils.HttpUtil;
import com.google.gson.Gson;

public class ProductWebApi implements WebApiContract<Product, String> {
	static final private String TAG = "ProductWebApi";
	
	public List<Product> getlist() throws ClientProtocolException, IOException {
		List<Product> products = new ArrayList<Product>();
		
		String url = buildListUrl(HOST, API_PRODUCT_URL, API_LIST_URL);
		Log.d(TAG, String.format("product list url: %s", url));			
		String json = HttpUtil.get(url);
		
		if (TextUtils.isEmpty(json)) {
			Log.w(TAG, "json is null or empty");				
			return products;
		}
		
		Log.d(TAG, String.format("Response from server: %s", json));
		
		Gson gson = new Gson();			
		products = gson.fromJson(json, ProductSet.class).getProducts();
		
		return products;
	}

	public String get(Product p) 
			throws ClientProtocolException, IOException {
		
		return null;
	}

	public String post(Product p) 
			throws NullPointerException, UnsupportedEncodingException, ClientProtocolException, IOException {
		if (p == null)
			throw new NullPointerException("Product passed as null to ProductWebApi post");	
		
		String url = buildEntityUrl(HOST, API_PRODUCT_URL);
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(NAME, p.getName()));
		pairs.add(new BasicNameValuePair(PRICE, p.getPrice()));
		
		String response = HttpUtil.post(url, pairs);
		
		if (TextUtils.isEmpty(response))
			throw new NullPointerException("ProductWebApi post returned null or empty result");
		
		return response;
	}

	public String put(Product p) 
			throws NullPointerException, UnsupportedEncodingException, 
				ClientProtocolException, IOException {
		if (p == null)
			throw new NullPointerException("Product passed as null to ProductWebApi put");
		
		String url = buildEntityUrl(HOST, API_PRODUCT_URL);
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(ID, p.getId()));
		pairs.add(new BasicNameValuePair(REV, p.getRev()));
		pairs.add(new BasicNameValuePair(NAME, p.getName()));
		pairs.add(new BasicNameValuePair(PRICE, p.getPrice()));  
		
		String response = HttpUtil.put(url, pairs);
		
		if (TextUtils.isEmpty(response))
			throw new NullPointerException("ProductWebApi put returned null or empty result");
		
		return response;
	}

	public String delete(Product p) 
			throws ClientProtocolException, IOException {
		if (p == null)
			throw new NullPointerException("Product passed as null to ProductWebApi delete");
		
		String url = buildDeleteUrl(HOST, API_PRODUCT_URL, p.getId(), p.getRev());
		Log.d(TAG, String.format("delete url: %s", url));
		
		String response = HttpUtil.delete(url);
		
		if (TextUtils.isEmpty(response))
			throw new NullPointerException("ProductWebApi delete returned null or empty result");
		
		return response;
	}
	
}
