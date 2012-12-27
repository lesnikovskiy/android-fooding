package com.fooding.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.adapters.ProductsDbAdapter;
import com.fooding.contracts.ProductDbContract;
import com.fooding.models.Product;

public class SelectProductActivity extends Activity implements OnItemClickListener {
	static final private String TAG = "SelectProductActivity";
	
	static final private String RECIPE_ID_KEY = "recipeId";
	
	private TextView recipeIdTextView;
	private EditText quantityEditText;
	private AutoCompleteTextView productNameAutocomplete;
	private ListView selectedProductList;
	private Button addProductButton;
	private EditText instructionsEditText;
	private Button saveRecipeButton;
	
	private ProductDbContract db;
	private CustomCursorAdapter customCursor;
	private Cursor cursor;
	private CustomArrayAdapter customAdapter;
	
	private List<Product> selectedProducts;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_product_layout);
		
		long id = getIntent().getExtras().getLong(RECIPE_ID_KEY, -1);
		if (id == -1) {
			Toast.makeText(this, "recipe id -1", Toast.LENGTH_LONG).show();
			finish();
		}
		
		recipeIdTextView = (TextView) findViewById(R.id.recipe_id);
		recipeIdTextView.setText(String.valueOf(id));
		quantityEditText = (EditText) findViewById(R.id.product_quantity);
		productNameAutocomplete = (AutoCompleteTextView) findViewById(R.id.select_product_autocomplete);
		selectedProductList = (ListView) findViewById(R.id.selected_products_list);	
		addProductButton = (Button) findViewById(R.id.add_product_to_recipe_button);
		instructionsEditText = (EditText) findViewById(R.id.instructions_text);
		saveRecipeButton = (Button) findViewById(R.id.save_recipe_button);
		
		selectedProducts = new ArrayList<Product>();
		
		customAdapter = 
				new CustomArrayAdapter(
						getApplicationContext(), R.layout.product_list_item, selectedProducts);
		selectedProductList.setAdapter(customAdapter);
	
		// init cursor
		db = new ProductsDbAdapter(this);
		db.open();
		cursor = db.getProductFindCursor("");
		
		startManagingCursor(cursor);
		
		customCursor = new CustomCursorAdapter(getApplicationContext(), cursor);
		
		// attach to adapter
		productNameAutocomplete.setAdapter(customCursor);
		productNameAutocomplete.setThreshold(1);
		productNameAutocomplete.setOnItemClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		
		db.close();
		cursor.close();
		customCursor.close();
	}	
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "onItemClick triggered.");
		
		long _id = -1;
		String name = null;
		String quantity = null;
		
		TextView idTextView = (TextView) view.findViewById(R.id.auto_product_id);
		if (idTextView != null) {
			_id = Long.parseLong(idTextView.getText().toString());
		}
		
		TextView nameTextView = (TextView) view.findViewById(R.id.auto_product_name);
		if (nameTextView != null) {
			name = nameTextView.getText().toString();
			productNameAutocomplete.setText(name);
		}
		
		TextView priceTextView = (TextView) view.findViewById(R.id.auto_product_price);
		if (priceTextView != null){
			quantity = quantityEditText.getText().toString();
		}
		
		if (id > 0 && name != null) {
			customAdapter.add(new Product(_id, name, quantity));
			customAdapter.notifyDataSetChanged();
		}
	}
	
	private class CustomArrayAdapter extends ArrayAdapter<Product> {
		private Context context;
		private int resourceId;
		private List<Product> products;
		
		private View view;

		public CustomArrayAdapter(Context context, int resourceId, List<Product> products) {
			super(context, resourceId, products);
			
			this.context = context;
			this.resourceId = resourceId;
			this.products = products;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			view = convertView;
			
			if (view == null) {
				LayoutInflater inflater = 
						(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(this.resourceId, parent, false);
			}
			
			Product product = products.get(position);
			
			if (product != null) {
				TextView idTextView = (TextView) view.findViewById(R.id.checkable_product_id);
				if (idTextView != null)
					idTextView.setText(String.valueOf(product.getId()));
				
				TextView nameTextView = (TextView) view.findViewById(R.id.checkable_product_name);
				if (nameTextView != null) 
					nameTextView.setText(product.getName());
			}
			
			return view;
		}
	}
	
	private class CustomCursorAdapter extends CursorAdapter {
		static final private String TAG = "CustomCursorAdapter";
		
		static final private int COLUMN_ID = 0;
		static final private int COLUMN_NAME = 1;
		static final private int COLUMN_PRICE = 2;
		
		private ProductDbContract db = null;
		
		public CustomCursorAdapter(Context context, Cursor c) {
			super(context, c);
			
			db = new ProductsDbAdapter(context);
			try {
			db.open();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String id = cursor.getString(COLUMN_ID);
			String name = cursor.getString(COLUMN_NAME);
			String price = cursor.getString(COLUMN_PRICE);
			
			TextView idTextView = (TextView) view.findViewById(R.id.auto_product_id);
			if (idTextView != null)
				idTextView.setText(id);
			
			TextView nameTextView = (TextView) view.findViewById(R.id.auto_product_name);
			if (nameTextView != null)
				nameTextView.setText(name);
			
			TextView priceTextView = (TextView) view.findViewById(R.id.auto_product_price);
			if (price != null)
				priceTextView.setText(price);
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final View view = 
					(View) inflater.inflate(R.layout.autocomplete_item_layout, parent, false);
			
			bindView(view, context, cursor);
			
			return view;
		}
		
		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			Cursor currentCursor = null;
			
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}
			
			String args = "";
			
			if (constraint != null) {
				args = constraint.toString();
			}
			
			currentCursor = db.getProductFindCursor(args);
			
			return currentCursor;
		}
		
		public void close() {
			db.close();
		}
	}		
}
