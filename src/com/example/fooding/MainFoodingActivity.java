package com.example.fooding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainFoodingActivity extends Activity implements OnClickListener {
	private EditText productNameEditText;
	private EditText productPriceEditText;
	
	final static private String WebServiceUrl = "http://fooding.jit.su/api/products/add";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fooding);
        
        View saveButton = this.findViewById(R.id.saveProductButton);
        saveButton.setOnClickListener(this);
        
        productNameEditText = (EditText) this.findViewById(R.id.productName);
        productPriceEditText = (EditText) this.findViewById(R.id.productPrice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_fooding, menu);
        return true;
    }
    
    public void onClick(View v) {
    	switch(v.getId()) {
    		case R.id.saveProductButton:
    			String name = productNameEditText.getText().toString();
    			String price = productPriceEditText.getText().toString();
    			Toast.makeText(this, "You are about to post " 
    					+ name + " " + price, Toast.LENGTH_LONG)
    				.show();
    			
    			HttpClient httpClient = new DefaultHttpClient();
    			
    			CookieStore cookieStore = new BasicCookieStore();
    			HttpContext context = new BasicHttpContext();
    			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    	
    			
    			HttpPost httppost = new HttpPost(WebServiceUrl);	
    			httppost.setHeader("Cookie", "foodingaccess=1-b8767f0228e22a1d3fe10e12e6d3d656");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("name", name));
					nameValuePairs.add(new BasicNameValuePair("price", price));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
					HttpResponse response = httpClient.execute(httppost);
					
					String line = "";
					StringBuilder result = new StringBuilder();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
					
					while ((line = reader.readLine()) != null) {
						result.append(line);
					}
					
					reader.close();
					
					Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
				break;
    	}
    }
    
    private String getFromUrl(String url) {
    	String result = "";
    	
    	HttpClient client = new DefaultHttpClient();
    	
    	CookieStore cookieStore = new BasicCookieStore();
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("Cookie", "foodingaccess=1-b8767f0228e22a1d3fe10e12e6d3d656");
		
		try {
			HttpResponse httpResponse = client.execute(httpget);
			
			String line = "";
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			
			reader.close();
			
			result = sb.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("Error", e.getMessage());
		}

		return result;
    }
}
