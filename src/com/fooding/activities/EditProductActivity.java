package com.fooding.activities;

import static com.fooding.utils.ProductConstants.ADD_FLAG;
import static com.fooding.utils.ProductConstants.EDIT_FLAG;
import static com.fooding.utils.ProductConstants.PRODUCTID;
import static com.fooding.utils.ProductConstants.NAME;
import static com.fooding.utils.ProductConstants.PRICE;

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
import com.fooding.models.Product;

public class EditProductActivity extends Activity implements OnClickListener {	
	private TextView idTextView;
	private EditText nameEditText;
	private EditText priceEditText;	
	
	private ProductDbContract db;
	
	final static private String TAG = "EditProductActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);
        
        idTextView = (TextView) this.findViewById(R.id.product_id);
        nameEditText = (EditText) this.findViewById(R.id.product_name);
        priceEditText = (EditText) this.findViewById(R.id.product_price);
        
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
        	String productid = intent.getExtras().getString(PRODUCTID);
	        String name = intent.getExtras().getString(NAME);
	        String price = intent.getExtras().getString(PRICE);        
	        
	        if (productid != null)
	        	idTextView.setText(productid);
	        
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
    	switch(v.getId()) {
    		case R.id.cancelButton:
    			EditProductActivity.this.setResult(RESULT_CANCELED);
    			finish();
    			break;
    		case R.id.addProductButton:   
    			try {
	    			String nName = nameEditText.getText().toString();
	    			double nPrice = Double.parseDouble(priceEditText.getText().toString());
	    			 
					db.insertProduct(new Product(nName, nPrice));				
					EditProductActivity.this.setResult(RESULT_OK);
    			} catch (NumberFormatException e) {
    				Log.e(TAG, "NumberFormatException when trying to add product");
    				Log.e(TAG, String.format("Data dump: [name: %s, price: %s]", 
    						nameEditText.getText().toString(), 
    							priceEditText.getText().toString()));
    				
    				EditProductActivity.this.setResult(RESULT_CANCELED);
    			} finally {
    				finish();
    			}
    			
    			break;
    		case R.id.saveProductButton:  
    			try {
	    			long uId = Long.parseLong(idTextView.getText().toString());
	    			String uName = nameEditText.getText().toString();
	    			double uPrice = Double.parseDouble(priceEditText.getText().toString()); 
        			
    				db.updateProduct(new Product(uId, uName, uPrice));
    				EditProductActivity.this.setResult(RESULT_OK);
    			} catch (NumberFormatException e) {
    				Log.e(TAG, "NumberFormatException when trying to update product");
    				Log.e(TAG, String.format("Data dump: [id: %s, name: %s, price: %s]", 
    						idTextView.getText().toString(),
    							nameEditText.getText().toString(), 
    								priceEditText.getText().toString()));
    				
    				EditProductActivity.this.setResult(RESULT_CANCELED);
    			} finally {
    				finish();
    			}
    			
				break;
    		case R.id.removeProductButton:   
    			try {
    				long dId = Long.parseLong(idTextView.getText().toString());
    				
    				db.deleteProduct(dId);
    				EditProductActivity.this.setResult(RESULT_OK);
    			} catch (NumberFormatException e) {
    				Log.e(TAG, "NumberFormatException when trying to delete product");
    				Log.e(TAG, String.format("Data dump: [id: %s]", 
    						idTextView.getText().toString()));
    				
    				EditProductActivity.this.setResult(RESULT_CANCELED);
    			} finally {		
    				finish();
    			}
    			
    			break;
    	}
    }
}
