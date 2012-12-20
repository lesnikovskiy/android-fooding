package com.fooding.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.adapters.ProductsDbAdapter;
import com.fooding.contracts.ProductDbContract;

public class SelectProductActivity extends Activity {
	static final private String TAG = "SelectProductActivity";
	
	static final private String RECIPE_ID_KEY = "recipeId";
	
	private TextView recipeIdTextView;
	private EditText priceEditText;
	private AutoCompleteTextView productNameAutocomplete;
	private ListView selectedProductList;
	
	private ProductDbContract db;
	private CustomCursorAdapter customCursor;
	private Cursor cursor;
		
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
		priceEditText = (EditText) findViewById(R.id.product_price);
		productNameAutocomplete = (AutoCompleteTextView) findViewById(R.id.product_name);
		selectedProductList = (ListView) findViewById(R.id.selected_products_list);
		
		// init cursor
		db = new ProductsDbAdapter(this);
		cursor = db.getProductFindCursor("");
		
		startManagingCursor(cursor);
		
		customCursor = new CustomCursorAdapter(getApplicationContext(), cursor);
		
		// attach to adapter
		productNameAutocomplete.setAdapter(customCursor);
		productNameAutocomplete.setThreshold(1);
	}
	
	private class CustomCursorAdapter extends CursorAdapter {
		private ProductDbContract db = null;
		
		public CustomCursorAdapter(Context context, Cursor c) {
			super(context, c);
			
			db = new ProductsDbAdapter(context);
			db.open();
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String item = createItem(cursor);
			((TextView) view).setText(item);
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = 
					(TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
			
			String item = createItem(cursor);
			view.setText(item);
			
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
		
		private String createItem(Cursor cursor) {
			String item = cursor.getString(1);
			return item;
		}
		
		public void close() {
			db.close();
		}
	}
}
