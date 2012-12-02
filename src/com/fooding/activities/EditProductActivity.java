package com.fooding.activities;

import static com.fooding.utils.Constants.ADD_FLAG;
import static com.fooding.utils.Constants.EDIT_FLAG;
import static com.fooding.utils.Constants.ID;
import static com.fooding.utils.Constants.NAME;
import static com.fooding.utils.Constants.PRICE;
import static com.fooding.utils.Constants.REV;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fooding.contracts.WebApiContract;
import com.fooding.entities.Product;
import com.fooding.webapi.ProductWebApi;

public class EditProductActivity extends Activity implements OnClickListener {
	private EditText productNameEditText;
	private EditText productPriceEditText;
	private TextView productIdTextView;
	private TextView productRevTextView;
	
	private WebApiContract<Product, String> webApi;
	
	final static private String TAG = "EditProductActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);
        
        productIdTextView = (TextView) this.findViewById(R.id.id);
        productRevTextView = (TextView) this.findViewById(R.id.rev);
        productNameEditText = (EditText) this.findViewById(R.id.productName);
        productPriceEditText = (EditText) this.findViewById(R.id.productPrice);
        
        webApi = new ProductWebApi();
        
        Intent intent = getIntent();
        boolean editFlag = intent.getExtras().getBoolean(EDIT_FLAG);
        boolean addFlag = intent.getExtras().getBoolean(ADD_FLAG);
        
        View addButton = this.findViewById(R.id.addProductButton);
        View saveButton = this.findViewById(R.id.saveProductButton);
        View removeButton = this.findViewById(R.id.removeProductButton);
        
        if (editFlag) {
	        String id = intent.getExtras().getString(ID);
	        String rev = intent.getExtras().getString(REV);
	        String name = intent.getExtras().getString(NAME);
	        String price = intent.getExtras().getString(PRICE);        
        
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
    			
    			Log.d(TAG, 
    					String.format("-X POST -d name: name=%s&price=%s", newProductName, newProductPrice));
    			
    			try {
					String response = webApi.post(new Product(null, null, newProductName, newProductPrice));				
					Log.d(TAG, String.format("Response from server: %s", response));
					EditProductActivity.this.setResult(RESULT_OK);
					finish();
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, String.format("UnsupportedEncodingException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (ClientProtocolException e) {
					Log.e(TAG, String.format("ClientProtocolException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (NullPointerException e) {
					Log.e(TAG, String.format("NullPointerException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (IOException e) {
					Log.e(TAG, String.format("IOException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				}
    			
    			break;
    		case R.id.saveProductButton:    			
    			String name = productNameEditText.getText().toString();
    			String price = productPriceEditText.getText().toString();    			
    			
    			Log.d(TAG, 
    					String.format("-X PUT -d id: id=%s&rev=%s&name=%s&price=%s", id, rev, name, price));    			
				try {
					String response = webApi.put(new Product(id, rev, name, price));				
					Log.d(TAG, String.format("Response from server: %s", response));
					EditProductActivity.this.setResult(RESULT_OK);
					finish();
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, String.format("UnsupportedEncodingException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (ClientProtocolException e) {
					Log.e(TAG, String.format("ClientProtocolException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (NullPointerException e) {
					Log.e(TAG, String.format("NullPointerException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (IOException e) {
					Log.e(TAG, String.format("IOException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				}
				
				break;
    		case R.id.removeProductButton:
    			Log.d(TAG, String.format("-X DELETE -d id=%s&rev=%s", id, rev));
    			
    			try {
    				String response = webApi.delete(new Product(id, rev, null, null));
    				Log.d(TAG, "Response from server: " + response);
    				EditProductActivity.this.setResult(RESULT_OK);
    				finish();
    			} catch (ClientProtocolException e) {
					Log.e(TAG, String.format("ClientProtocolException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (NullPointerException e) {
					Log.e(TAG, String.format("NullPointerException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				} catch (IOException e) {
					Log.e(TAG, String.format("IOException: %s", e.getMessage()));
					EditProductActivity.this.setResult(RESULT_CANCELED);
					finish();
				}
    			
    			break;
    	}
    }
}
