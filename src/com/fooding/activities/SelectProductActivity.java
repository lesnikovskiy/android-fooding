package com.fooding.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.contracts.ProductDbContract;
import com.fooding.contracts.RecipesContract;
import com.fooding.models.Product;
import com.fooding.models.Recipe;

public class SelectProductActivity extends Activity implements OnItemClickListener, OnClickListener {
	static final private String TAG = "SelectProductActivity";
	
	static final private String RECIPE_ID_KEY = "recipeId";
	static final private String RECIPE_ID_NEXT = "recipe_id";
	
	private TextView recipeIdTextView;
	private TextView recipeNameTextView;
	private TextView productIdTextView;
	private EditText quantityEditText;
	private AutoCompleteTextView productNameAutocomplete;
	private ListView selectedProductList;
	private Button addProductButton;
	private Button nextButton;
	
	private ProductDbContract db;
	private RecipesContract recipesDB;
	private CustomCursorAdapter customCursor;
	private Cursor cursor;
	private CustomArrayAdapter customAdapter;
	
	private List<Product> selectedProducts;
	private Recipe currentRecipe;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_product_layout);
		
		long id = getIntent().getExtras().getLong(RECIPE_ID_KEY, -1);
		
		try {
			recipesDB = new RecipeDbAdapter(getApplicationContext());		
			recipesDB.open();
		} catch (Exception e) {
			Log.e(TAG, String.format("Error occurred: %s", e.getMessage()));
		}
		
		currentRecipe = recipesDB.getById(id);
		
		//String recipeName = getIntent().getExtras().getString(RECIPE_NAME_KEY);
		if (currentRecipe == null) {
			Toast.makeText(this, "Recipe not found", Toast.LENGTH_LONG).show();
			finish();
		}
		
		recipeIdTextView = (TextView) findViewById(R.id.recipe_id);
		recipeIdTextView.setText(String.valueOf(id));
		recipeNameTextView = (TextView) findViewById(R.id.recipe_name_viewstate);
		recipeNameTextView.setText(currentRecipe.getName());
		
		productIdTextView = (TextView) findViewById(R.id.selected_product_id);
		quantityEditText = (EditText) findViewById(R.id.product_quantity);
		productNameAutocomplete = (AutoCompleteTextView) findViewById(R.id.select_product_autocomplete);
		selectedProductList = (ListView) findViewById(R.id.selected_products_list);	
		addProductButton = (Button) findViewById(R.id.add_product_to_recipe_button);
		nextButton = (Button) findViewById(R.id.next_to_instructions_button);
		
		selectedProducts = new ArrayList<Product>();
		
		customAdapter = 
				new CustomArrayAdapter(
						getApplicationContext(), R.layout.select_product_item, selectedProducts);
		selectedProductList.setAdapter(customAdapter);
	
		// init cursor
		db = new ProductsDbAdapter(this);
		db.open();
		
		// get available products
		List<Product> insertedProducts = db.getProductsByRecipes(new long[] {id});
		if (insertedProducts.size() > 0) {
			for (Product p : insertedProducts) {
				customAdapter.add(p);
			}
			customAdapter.notifyDataSetChanged();
		}
		
		cursor = db.getProductFindCursor("");		
		startManagingCursor(cursor);	
		
		customCursor = new CustomCursorAdapter(getApplicationContext(), cursor);
		
		
		
		// attach to adapter
		productNameAutocomplete.setAdapter(customCursor);
		productNameAutocomplete.setThreshold(1);
		
		productNameAutocomplete.setOnItemClickListener(this);
		addProductButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		
		db.close();
		cursor.close();
		customCursor.close();
		recipesDB.close();
	}	
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "onItemClick triggered.");
		
		TextView idTextView = (TextView) view.findViewById(R.id.auto_product_id);
		if (idTextView != null) {
			productIdTextView.setText(idTextView.getText().toString());		
		}
		
		TextView nameTextView = (TextView) view.findViewById(R.id.auto_product_name);
		if (nameTextView != null) {
			productNameAutocomplete.setText(nameTextView.getText().toString());
		}
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.add_product_to_recipe_button:
			String id = productIdTextView.getText().toString();
			long recipeId = Long.parseLong(recipeIdTextView.getText().toString());
			long _id = -1;
			if (!TextUtils.isEmpty(id) && TextUtils.isDigitsOnly(id)) {
				_id = Long.parseLong(id);
			}
			
			String name = productNameAutocomplete.getText().toString();
			String quantity = quantityEditText.getText().toString();
			if (_id > 0 && !TextUtils.isEmpty(name)) {				
				customAdapter.add(new Product(_id, name, quantity));
				customAdapter.notifyDataSetChanged();				
			} else {
				db.insertProduct(new Product(name, 0.0));
				long insertId = db.getLastInsertId();
				_id = insertId;
				if (insertId > 0) {
					customAdapter.add(new Product(insertId, name, quantity));
					customAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getApplicationContext(), 
							"Cannot add product! Something bad happened!", Toast.LENGTH_LONG).show();
				}
			}
			
			boolean isInserted = recipesDB.addProductToRecipe(_id, recipeId, quantity);
			Log.d(TAG, String.format("product_to_recipe insert status successful: %s", isInserted));
			
			productIdTextView.setText("");
			productNameAutocomplete.setText("");
			quantityEditText.setText("");
			
			break;
		case R.id.next_to_instructions_button:
			Intent intent = new Intent(getApplicationContext(), InstructionsActivity.class);
			intent.putExtra(RECIPE_ID_NEXT, currentRecipe.getId());
			startActivity(intent);
			break;
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
				TextView idTextView = (TextView) view.findViewById(R.id.checkable_selected_product_id);
				if (idTextView != null)
					idTextView.setText(String.valueOf(product.getId()));
				
				TextView nameTextView = (TextView) view.findViewById(R.id.checkable_selected_product_name);
				if (nameTextView != null) 
					nameTextView.setText(product.getName());
				
				TextView quantityTextView = 
						(TextView) view.findViewById(R.id.checkable_selected_product_quantity);
				if (quantityTextView != null)
					quantityTextView.setText(product.getQuantity());
			}
			
			return view;
		}
	}
	
	private class CustomCursorAdapter extends CursorAdapter {
		static final private String TAG = "CustomCursorAdapter";
		
		static final private int COLUMN_ID = 0;
		static final private int COLUMN_NAME = 1;
		
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
			
			TextView idTextView = (TextView) view.findViewById(R.id.auto_product_id);
			if (idTextView != null)
				idTextView.setText(id);
			
			TextView nameTextView = (TextView) view.findViewById(R.id.auto_product_name);
			if (nameTextView != null)
				nameTextView.setText(name);
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
