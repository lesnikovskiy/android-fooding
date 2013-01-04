package com.fooding.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
	static final private String RECIPE_NAME_KEY = "recipeName";
	
	private TextView recipeIdTextView;
	private TextView recipeNameTextView;
	private TextView productIdTextView;
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
		String recipeName = getIntent().getExtras().getString(RECIPE_NAME_KEY);
		if (id == -1) {
			Toast.makeText(this, "recipe id -1", Toast.LENGTH_LONG).show();
			finish();
		}
		
		recipeIdTextView = (TextView) findViewById(R.id.recipe_id);
		recipeIdTextView.setText(String.valueOf(id));
		recipeNameTextView = (TextView) findViewById(R.id.recipe_name_viewstate);
		recipeNameTextView.setText(recipeName);
		
		productIdTextView = (TextView) findViewById(R.id.selected_product_id);
		quantityEditText = (EditText) findViewById(R.id.product_quantity);
		productNameAutocomplete = (AutoCompleteTextView) findViewById(R.id.select_product_autocomplete);
		selectedProductList = (ListView) findViewById(R.id.selected_products_list);	
		addProductButton = (Button) findViewById(R.id.add_product_to_recipe_button);
		instructionsEditText = (EditText) findViewById(R.id.instructions_text);
		saveRecipeButton = (Button) findViewById(R.id.save_recipe_button);
		
		selectedProducts = new ArrayList<Product>();
		
		customAdapter = 
				new CustomArrayAdapter(
						getApplicationContext(), R.layout.select_product_item, selectedProducts);
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
		addProductButton.setOnClickListener(this);
		saveRecipeButton.setOnClickListener(this);
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
				if (insertId > 0) {
					customAdapter.add(new Product(insertId, name, quantity));
				} else {
					Toast.makeText(getApplicationContext(), 
							"Cannot add product! Something bad happened!", Toast.LENGTH_LONG).show();
				}
			}
			
			productIdTextView.setText("");
			productNameAutocomplete.setText("");
			quantityEditText.setText("");
			
			break;
		case R.id.save_recipe_button:
			long recipeID = Long.parseLong(recipeIdTextView.getText().toString());
			String recipeName = recipeNameTextView.getText().toString();
			String instructions = instructionsEditText.getText().toString();
			
			RecipesContract recipesDB = new RecipeDbAdapter(getApplicationContext());
			recipesDB.open();
			
			boolean isUpdated = recipesDB.updateRecipe(new Recipe(recipeID, recipeName, instructions));
			if (isUpdated)
				Log.d(TAG, "Recipe successfully updated");
			
			for (int i = 0; i < selectedProductList.getChildCount(); i++) {
				View childView = selectedProductList.getChildAt(i);
				
				long prdId = -1;
				String prdName = "";
				String prdQty = "";
				
				TextView prodIdTV = 
						(TextView) childView.findViewById(R.id.checkable_selected_product_id);
				if (prodIdTV != null)
					prdId = Long.parseLong(prodIdTV.getText().toString());
				
				TextView prodNameTV =
						(TextView) childView.findViewById(R.id.checkable_selected_product_name);
				if (prodNameTV != null)
					prdName = prodNameTV.getText().toString();
				
				TextView prodQntyTV =
						(TextView) childView.findViewById(R.id.checkable_selected_product_quantity);
				if (prodQntyTV != null)
					prdQty = prodQntyTV.getText().toString();
				
				Log.d(TAG, String.format("At position %s: %s %s %s", i, prdId, prdName, prdQty));
				
				boolean isInserted = recipesDB.addProductToRecipe(prdId, recipeID, prdQty);
				
				Log.d(TAG, String.format("Product insert status successful: %s", isInserted));			
			}
			
			recipesDB.close();
			Toast.makeText(getApplicationContext(), "Recipe successfully saved", Toast.LENGTH_LONG).show();
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
