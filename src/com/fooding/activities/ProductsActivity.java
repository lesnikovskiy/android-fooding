package com.fooding.activities;

import static com.fooding.utils.Constants.ADD_FLAG;
import static com.fooding.utils.Constants.ADD_PRODUCTS_RESULT;
import static com.fooding.utils.Constants.EDIT_FLAG;
import static com.fooding.utils.Constants.EDIT_PRODUCTS_RESULT;
import static com.fooding.utils.Constants.ID;
import static com.fooding.utils.Constants.NAME;
import static com.fooding.utils.Constants.PRICE;
import static com.fooding.utils.Constants.REV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fooding.adapters.ProductArrayAdapter;
import com.fooding.contracts.WebApiContract;
import com.fooding.entities.Product;
import com.fooding.webapi.ProductWebApi;

public class ProductsActivity extends Activity implements OnItemClickListener  {
	private final static String TAG = "ProductsActivity";
	
	private ListView listView;
	private ProductArrayAdapter productsArrayAdapter;
	private List<Product> products;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.products_layout);
		
		products = getProductList();
		if (products != null && products.size() > 0) {
			listView = (ListView) findViewById(R.id.products);
			productsArrayAdapter = new ProductArrayAdapter(this, R.layout.product_list_item, products);
			listView.setAdapter(this.productsArrayAdapter);
			listView.setOnItemClickListener(this);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.products, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_new_product:
				Intent intent = new Intent(this, EditProductActivity.class);
				intent.putExtra(ADD_FLAG, true);
				intent.putExtra(EDIT_FLAG, false);
				startActivityForResult(intent, ADD_PRODUCTS_RESULT);				
				return true;
			case R.id.menu_settings:
				Log.d(TAG, "You selected menu settings");
				return true;
		}
		
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult");
		Log.d(TAG, "requestCode: " + requestCode);
		Log.d(TAG, "resultCode: " + resultCode);
		
		switch(requestCode) {
			case EDIT_PRODUCTS_RESULT:
			case ADD_PRODUCTS_RESULT:
				if (resultCode == Activity.RESULT_OK) {
					products.clear();
					products = getProductList();
					Log.w(TAG, 
							String.format("onActivityResult getProductList returned %s", 
									products.toString()));
					if (products != null && products.size() > 0) {
						productsArrayAdapter = 
								new ProductArrayAdapter(this, R.layout.product_list_item, products);
						listView.setAdapter(productsArrayAdapter);
						listView.setOnItemClickListener(this);
					}
				}
				break;
		}
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		View v = listView.getChildAt(position);
		TextView idText = (TextView) v.findViewById(R.id.id);
		TextView revText = (TextView) v.findViewById(R.id.rev);
		TextView nameText = (TextView) v.findViewById(R.id.name);
		TextView priceText = (TextView) v.findViewById(R.id.price);
		
		Intent intent = new Intent(v.getContext(), EditProductActivity.class);
		intent.putExtra(EDIT_FLAG, true);
		intent.putExtra(ADD_FLAG, false);
		if (idText != null)
			intent.putExtra(ID, idText.getText().toString());						
		if(revText != null)
			intent.putExtra(REV, revText.getText().toString());
		if(nameText != null)
			intent.putExtra(NAME, nameText.getText().toString());
		if(priceText != null)
			intent.putExtra(PRICE, priceText.getText().toString());
		
		startActivityForResult(intent, EDIT_PRODUCTS_RESULT);
	}
	
	private List<Product> getProductList() {
		List<Product> products = new ArrayList<Product>();
		try {	
			WebApiContract<Product, String> webApi = new ProductWebApi();
			products = webApi.getlist();						
		} catch (ClientProtocolException e) {
			String errorMessage = "ClientProtocolException: " + e.getMessage() + e.getStackTrace().toString();
			Log.e(TAG, errorMessage);
		} catch (IOException e) {
			String errorMessage = "IOException: " + e.getMessage() + e.getStackTrace().toString();
			Log.e(TAG, errorMessage);
		}
		catch (Exception e) {
			String errorMessage = "Exception: " + e.getMessage() + e.getStackTrace().toString();
			Log.e(TAG, errorMessage);
		}
		
		Log.d(TAG, "getProductList returned: " + products.toString());
		
		return products;
	}
}
