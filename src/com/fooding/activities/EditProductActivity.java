package com.fooding.activities;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fooding.utils.Constants;
import com.fooding.utils.HttpUtil;

public class EditProductActivity extends Activity implements OnClickListener {
	private EditText productNameEditText;
	private EditText productPriceEditText;
	private TextView productIdTextView;
	private TextView productRevTextView;
	
	final static private String TAG = "EditProductActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);
        
        productIdTextView = (TextView) this.findViewById(R.id.id);
        productRevTextView = (TextView) this.findViewById(R.id.rev);
        productNameEditText = (EditText) this.findViewById(R.id.productName);
        productPriceEditText = (EditText) this.findViewById(R.id.productPrice);
        
        Intent intent = getIntent();
        boolean editFlag = intent.getExtras().getBoolean(Constants.EDIT_FLAG);
        boolean addFlag = intent.getExtras().getBoolean(Constants.ADD_FLAG);
        
        View addButton = this.findViewById(R.id.addProductButton);
        View saveButton = this.findViewById(R.id.saveProductButton);
        View removeButton = this.findViewById(R.id.removeProductButton);
        
        if (editFlag) {
	        String id = intent.getExtras().getString(Constants.ID);
	        String rev = intent.getExtras().getString(Constants.REV);
	        String name = intent.getExtras().getString(Constants.NAME);
	        String price = intent.getExtras().getString(Constants.PRICE);        
        
	        if (id != null)
	        	productIdTextView.setText(id);
	        
	        if (rev != null)
	        	productRevTextView.setText(rev);
	        
	        if (name != null)
	        	productNameEditText.setText(name);
	        
	        if (price != null)
	        	productPriceEditText.setText(price);
	        
	        
	        addButton.setVisibility(View.GONE);	        
	        saveButton.setOnClickListener(this);	        
	        removeButton.setOnClickListener(this);        
        }
        
        if (addFlag) {
        	addButton.setOnClickListener(this);        
	        saveButton.setVisibility(View.GONE);	        
	        removeButton.setVisibility(View.GONE);
        }
        
        View cancelButton = this.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	String id = productIdTextView.getText().toString();
		String rev = productRevTextView.getText().toString();
    	
    	switch(v.getId()) {
    		case R.id.cancelButton:
    			EditProductActivity.this.setResult(RESULT_CANCELED);
    			finish();
    			break;
    		case R.id.addProductButton:
    			String newProductName = productNameEditText.getText().toString();
    			String newProductPrice = productPriceEditText.getText().toString();
    			
    			Log.d(TAG, "Sending post request: name=" + newProductName + "&price=" + newProductPrice);
    			
    			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    			pairs.add(new BasicNameValuePair(Constants.NAME, newProductName));
    			pairs.add(new BasicNameValuePair(Constants.PRICE, newProductPrice));
    			
    			try {
					String response = HttpUtil.post(Constants.API_ADD_PRODUCT_URL, pairs);					
					Log.d(TAG, "Response from server: " + response);
					EditProductActivity.this.setResult(RESULT_OK);
					finish();
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (ClientProtocolException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				}
    			
    			break;
    		case R.id.saveProductButton:    			
    			String name = productNameEditText.getText().toString();
    			String price = productPriceEditText.getText().toString();    			
    			
    			Log.d(TAG, "Sending post request: id="
    					+ id + "&rev="
    					+ rev + "&name="
    					+ name + "&price=" + price);
    			
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    			nameValuePairs.add(new BasicNameValuePair(Constants.ID, id));
    			nameValuePairs.add(new BasicNameValuePair(Constants.REV, rev));
				nameValuePairs.add(new BasicNameValuePair(Constants.NAME, name));
				nameValuePairs.add(new BasicNameValuePair(Constants.PRICE, price));   		   	
    			
				try {
					String response = HttpUtil.post(Constants.API_UPDATE_PRODUCT_URL, nameValuePairs);					
					Log.d(TAG, "Response from server: " + response);
					EditProductActivity.this.setResult(RESULT_OK);
					finish();
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (ClientProtocolException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				}
				
				break;
    		case R.id.removeProductButton:
    			Log.d(TAG, "Sending post request: id=" + id + "&rev=" + rev);
    			
    			List<NameValuePair> deletePairs = new ArrayList<NameValuePair>();
    			deletePairs.add(new BasicNameValuePair(Constants.ID, id));
    			deletePairs.add(new BasicNameValuePair(Constants.REV, rev));
    			
    			try {
    				String response = HttpUtil.post(Constants.API_REMOVE_PRODUCT_URL, deletePairs);
    				Log.d(TAG, "Response from server: " + response);
    				EditProductActivity.this.setResult(RESULT_OK);
    				finish();
    			} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (ClientProtocolException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				}
    			
    			break;
    	}
    }
}
