package com.example.fooding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class EditProductActivity extends Activity implements OnClickListener {
	private EditText productNameEditText;
	private EditText productPriceEditText;
	private TextView productIdTextView;
	private TextView productRevTextView;
	
	final static private String WEB_SERVICE_URL = "http://fooding.jit.su/api/products/update";	
	final static private String API_REMOVE_URL = "http://fooding.jit.su/api/products/remove";
	final static private String TAG = "EditProductActivity";
	
	final static private String ID = "id";
	final static private String REV = "rev";
	final static private String NAME = "name";
	final static private String PRICE = "price";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);
        
        Intent intent = getIntent();
        String id = intent.getExtras().getString(ID);
        String rev = intent.getExtras().getString(REV);
        String name = intent.getExtras().getString(NAME);
        String price = intent.getExtras().getString(PRICE);      
        
        productIdTextView = (TextView) this.findViewById(R.id.id);
        productRevTextView = (TextView) this.findViewById(R.id.rev);
        productNameEditText = (EditText) this.findViewById(R.id.productName);
        productPriceEditText = (EditText) this.findViewById(R.id.productPrice);
        
        if (id != null)
        	productIdTextView.setText(id);
        
        if (rev != null)
        	productRevTextView.setText(rev);
        
        if (name != null)
        	productNameEditText.setText(name);
        
        if (price != null)
        	productPriceEditText.setText(price);
        
        View saveButton = this.findViewById(R.id.saveProductButton);
        saveButton.setOnClickListener(this);
        
        View removeButton = this.findViewById(R.id.removeProductButton);
        removeButton.setOnClickListener(this);
        
        View cancelButton = this.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_fooding, menu);
        return true;
    }
    
    public void onClick(View v) {
    	String id = productIdTextView.getText().toString();
		String rev = productRevTextView.getText().toString();
    	
    	switch(v.getId()) {
    		case R.id.cancelButton:
    			finish();
    			break;
    		case R.id.saveProductButton:    			
    			String name = productNameEditText.getText().toString();
    			String price = productPriceEditText.getText().toString();    			
    			
    			Log.d(TAG, "Sending post request: id="
    					+ id + "&rev="
    					+ rev + "&name="
    					+ name + "&price=" + price);
    			
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    			nameValuePairs.add(new BasicNameValuePair(ID, id));
    			nameValuePairs.add(new BasicNameValuePair(REV, rev));
				nameValuePairs.add(new BasicNameValuePair(NAME, name));
				nameValuePairs.add(new BasicNameValuePair(PRICE, price));   		   	
    			
				try {
					String response = HttpUtil.post(WEB_SERVICE_URL, nameValuePairs);					
					Log.d(TAG, "Response from server: " + response);
					finish();
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage());
				} catch (ClientProtocolException e) {
					Log.e(TAG, e.getMessage());
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
				
				break;
    		case R.id.removeProductButton:
    			Log.d(TAG, "Sending post request: id=" + id + "&rev=" + rev);
    			
    			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    			pairs.add(new BasicNameValuePair(ID, id));
    			pairs.add(new BasicNameValuePair(REV, rev));
    			
    			try {
    				String response = HttpUtil.post(API_REMOVE_URL, pairs);
    				Log.d(TAG, "Response from server: " + response);
    				finish();
    			} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage());
				} catch (ClientProtocolException e) {
					Log.e(TAG, e.getMessage());
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
    			
    			break;
    	}
    }
}
