package com.example.fooding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainFoodingActivity extends Activity implements OnClickListener {
	private String productName;
	private String productPrice;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fooding);
        
        View saveButton = this.findViewById(R.id.saveProductButton);
        saveButton.setOnClickListener(this);
        
        EditText productNameView = (EditText) this.findViewById(R.id.productName);
        productName = productNameView.getText().toString();
        EditText productPriceView = (EditText) this.findViewById(R.id.productPrice);
        productPrice = productPriceView.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_fooding, menu);
        return true;
    }
    
    public void onClick(View v) {
    	switch(v.getId()) {
    		case R.id.saveProductButton:
    			HttpClient httpClient = new DefaultHttpClient();
    			
    			CookieStore cookieStore = new BasicCookieStore();
    			HttpContext context = new BasicHttpContext();
    			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    	
    			
    			HttpPost httppost = new HttpPost("http://fooding.jit.su/api/products/add");	
    			httppost.setHeader("Cookie", "foodingaccess=1-b8767f0228e22a1d3fe10e12e6d3d656");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("name", productName));
					nameValuePairs.add(new BasicNameValuePair("price", productPrice));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
					HttpResponse response = httpClient.execute(httppost);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
    	}
    }
}
