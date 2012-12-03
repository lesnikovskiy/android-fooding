package com.fooding.activities;

import static com.fooding.utils.Constants.ADD_FLAG;
import static com.fooding.utils.Constants.EDIT_FLAG;
import static com.fooding.utils.Constants.ID;
import static com.fooding.utils.Constants.NAME;
import static com.fooding.utils.Constants.PRICE;
import static com.fooding.utils.Constants.PRODUCT_ID;
import static com.fooding.utils.Constants.REV;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fooding.adapters.ProductsDbAdapter;
import com.fooding.contracts.ProductDbContract;
import com.fooding.contracts.WebApiContract;
import com.fooding.entities.Product;
import com.fooding.webapi.ProductWebApi;

public class EditProductActivity extends Activity implements OnClickListener {	
	private TextView productidTextView;
	private TextView idTextView;
	private TextView revTextView;
	private EditText nameEditText;
	private EditText priceEditText;	
	
	private WebApiContract<Product, String> webApi;
	private ProductDbContract db;
	
	final static private String TAG = "EditProductActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);
        
        productidTextView = (TextView) this.findViewById(R.id.product_id);
        idTextView = (TextView) this.findViewById(R.id.id);
        revTextView = (TextView) this.findViewById(R.id.rev);
        nameEditText = (EditText) this.findViewById(R.id.productName);
        priceEditText = (EditText) this.findViewById(R.id.productPrice);
        
        webApi = new ProductWebApi();
        db = new ProductsDbAdapter(this);
        
        try {
        	db.open();
        } catch (SQLiteException e) {
        	Log.e(TAG, e.getMessage());
        }
        
        Intent intent = getIntent();
        boolean editFlag = intent.getExtras().getBoolean(EDIT_FLAG);
        boolean addFlag = intent.getExtras().getBoolean(ADD_FLAG);
        
        View addButton = this.findViewById(R.id.addProductButton);
        View saveButton = this.findViewById(R.id.saveProductButton);
        View removeButton = this.findViewById(R.id.removeProductButton);
        
        if (editFlag) {
        	String productid = intent.getExtras().getString(PRODUCT_ID);
	        String id = intent.getExtras().getString(ID);
	        String rev = intent.getExtras().getString(REV);
	        String name = intent.getExtras().getString(NAME);
	        String price = intent.getExtras().getString(PRICE);        
	        
	        if (productid != null)
	        	productidTextView.setText(productid);
	        
	        if (id != null)
	        	idTextView.setText(id);
	        
	        if (rev != null)
	        	revTextView.setText(rev);
	        
	        if (name != null)
	        	nameEditText.setText(name);
	        
	        if (price != null)
	        	priceEditText.setText(price);	        
	        
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
    
    @Override
    protected void onDestroy() {
    	db.close();
    	super.onDestroy();
    }
    
    public void onClick(View v) {
    	String id = idTextView.getText().toString();
		String rev = revTextView.getText().toString();
    	
    	switch(v.getId()) {
    		case R.id.cancelButton:
    			EditProductActivity.this.setResult(RESULT_CANCELED);
    			finish();
    			break;
    		case R.id.addProductButton:
    			String newProductName = nameEditText.getText().toString();
    			String newProductPrice = priceEditText.getText().toString();
    			
    			Product newProduct = new Product(null, null, newProductName, newProductPrice);   			
    			
    			Log.d(TAG, 
    					String.format("-X POST -d name: name=%s&price=%s", newProductName, newProductPrice));
    			
    			try {
    				db.insertProduct(newProduct);
    				
					String response = webApi.post(newProduct);				
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
    			String productid = productidTextView.getText().toString();
    			String name = nameEditText.getText().toString();
    			String price = priceEditText.getText().toString();    
    			
    			long pid = 0;
    			try {
    				pid = Long.parseLong(productid);
    			} catch (NumberFormatException e) {
    				Log.e(TAG, String.format("NumberFormatException: %s", e.getMessage()));
    			}
    			
    			Product udpProduct = new Product(pid, id, rev, name, price);
    			
    			Log.d(TAG, 
    					String.format("-X PUT -d id: id=%s&rev=%s&name=%s&price=%s", id, rev, name, price));    			
				try {
					db.updateProduct(udpProduct);
					
					String response = webApi.put(udpProduct);				
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
    			long prodId = 0;
    			try {
    				prodId = Long.parseLong(productidTextView.getText().toString());
    			} catch (NumberFormatException e) {
    				Log.e(TAG, String.format("NumberFormatException: %s", e.getMessage()));
    			}
    			
    			Log.d(TAG, String.format("-X DELETE -d id=%s&rev=%s", id, rev));
    			Product delProduct = new Product(prodId, id, rev, null, null);
    			
    			try {
    				db.deleteProduct(delProduct.getProductId());
    				
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
