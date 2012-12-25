package com.fooding.activities;

import static com.fooding.utils.ProductConstants.ADD_FLAG;
import static com.fooding.utils.ProductConstants.ADD_PRODUCT_RESULT;
import static com.fooding.utils.ProductConstants.EDIT_FLAG;
import static com.fooding.utils.ProductConstants.EDIT_PRODUCTS_RESULT;
import static com.fooding.utils.ProductConstants.ID;
import static com.fooding.utils.ProductConstants.NAME;
import static com.fooding.utils.ProductConstants.PRICE;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fooding.adapters.ProductsDbAdapter;
import com.fooding.contracts.ProductDbContract;
import com.fooding.models.Product;

public class ProductsActivity extends Activity implements OnItemClickListener  {
	private final static String TAG = "ProductsActivity";
	
	private ListView listView;
	private ProductArrayAdapter productsArrayAdapter;
	private List<Product> products;
	private ProductDbContract db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.products_layout);
		
		db = new ProductsDbAdapter(this);
		
		try {
			db.open();
		} catch (SQLiteException e) {
			Log.e(TAG, String.format("SQLiteException: %s", e.getMessage()));
		}
		
		products = getProductListFromDb();
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
				startActivityForResult(intent, ADD_PRODUCT_RESULT);				
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
			case ADD_PRODUCT_RESULT:
				if (resultCode == Activity.RESULT_OK) {
					products.clear();
					products = getProductListFromDb();					
					Log.d(TAG, 
							String.format("onActivityResult getProductList returned %s", 
									products.toString()));
					if (products != null && products.size() > 0) {
						try {
							productsArrayAdapter = 
									new ProductArrayAdapter(this, R.layout.product_list_item, products);
							listView.setAdapter(productsArrayAdapter);
							productsArrayAdapter.notifyDataSetChanged();
							listView.setOnItemClickListener(this);
						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
						}
					}
				}
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		View v = listView.getChildAt(position);
		TextView productIdText = (TextView) v.findViewById(R.id.checkable_product_id);
		TextView nameText = (TextView) v.findViewById(R.id.checkable_product_name);
		TextView priceText = (TextView) v.findViewById(R.id.checkable_product_price);
		
		Intent intent = new Intent(v.getContext(), EditProductActivity.class);
		intent.putExtra(EDIT_FLAG, true);
		intent.putExtra(ADD_FLAG, false);
		if (productIdText != null)
			intent.putExtra(ID, productIdText.getText().toString());	
		if(nameText != null)
			intent.putExtra(NAME, nameText.getText().toString());
		if(priceText != null)
			intent.putExtra(PRICE, priceText.getText().toString());
		
		startActivityForResult(intent, EDIT_PRODUCTS_RESULT);
	}
	
	private final List<Product> getProductListFromDb() {		
		return db.getProducts();
	}
	
	private class ProductArrayAdapter extends ArrayAdapter<Product> {
		private View view;
		private Context context;
		private int resource;
		private List<Product> products;		
		
		
		public ProductArrayAdapter (Context context, int resource, List<Product> products) {
			super(context, resource, products);
			
			this.context = context;
			this.resource = resource;
			this.products = products;		
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			view = convertView;
			
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(resource, parent, false);
			}
			
			Product product = products.get(position);
			
			if (product == null)
				return view;
			
			TextView idTextView = (TextView) view.findViewById(R.id.checkable_product_id);
			if (idTextView != null)
				idTextView.setText(String.valueOf(product.getId()));
			
			TextView nameTextView = (TextView) view.findViewById(R.id.checkable_product_name);
			if (nameTextView != null)
				nameTextView.setText(product.getName());
			
			TextView priceTextView = (TextView) view.findViewById(R.id.checkable_product_price);
			if (priceTextView != null)
				priceTextView.setText(String.valueOf(product.getPrice()));
				
			return view;
		}
	}
}
