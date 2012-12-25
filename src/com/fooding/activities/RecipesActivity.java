package com.fooding.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.contracts.RecipesContract;
import com.fooding.models.Recipe;

public class RecipesActivity extends Activity {
	static final private String TAG = "RecipesActivity";
	static final private int MENU_ADD = 100;
	static final private int CALC_ITEM = 200;
	
	private RecipesContract db;
	private ListView recipesListView;
	private RecipeArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipes_layout);
		
		recipesListView = (ListView) findViewById(R.id.recipes_list_view);
		
		db = new RecipeDbAdapter(getApplicationContext());
		db.open();
		
		List<Recipe> recipes = db.getRecipes();
		Log.d(TAG, String.format("Retrieved %s number of recipes", recipes.size()));
		
		if (recipes.size() > 0) {
			adapter = new RecipeArrayAdapter(getApplicationContext(), 
							R.layout.checkable_item_layout, recipes);
			recipesListView.setAdapter(adapter);
			recipesListView.setItemsCanFocus(false);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		MenuItem addItem = menu.add(0, MENU_ADD, Menu.NONE, R.string.recipes_add_recipe_menu_item);
		addItem.setShortcut('0', 'a');
		addItem.setIntent(new Intent(getApplicationContext(), AddRecipeActivity.class));
		
		MenuItem calcRecipe = menu.add(0, CALC_ITEM, Menu.NONE, R.string.recipes_calc_recipes);
		calcRecipe.setShortcut('1', 'c');
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case CALC_ITEM:
			List<Recipe> selectedRecipes = new ArrayList<Recipe>();
			final SparseBooleanArray checkedItems = recipesListView.getCheckedItemPositions();
			for (int i = 0; i < checkedItems.size(); i++) {
				int position = checkedItems.keyAt(i);
				if (checkedItems.valueAt(i)) {
					selectedRecipes.add(adapter.getItem(position));
					Log.d(TAG, "selected item: " + adapter.getItem(position).getName());
				}
			}
			 
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
	private class RecipeArrayAdapter extends ArrayAdapter<Recipe> {
		static final private String TAG = "RecipeArrayAdapter";
		
		private View view;
		private Context context;
		private int resource;
		private List<Recipe> recipes;		
		
		
		public RecipeArrayAdapter(Context context, int resource, List<Recipe> recipes) {
			super(context, resource, recipes);
			
			this.context = context;
			this.resource = resource;
			this.recipes = recipes;			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			view = convertView;			
			
			if (view == null) {				
				LayoutInflater inflater = 
						(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(resource, parent, false);
			}
			
			Recipe recipe = recipes.get(position);
			
			if (recipe != null) {
				Log.d(TAG, 
						String.format("Recipe retrieved at position %s: %s - %s", 
								position, recipe.getId(), recipe.getName()));
				TextView idTextView = (TextView) view.findViewById(R.id.checkable_id);
				if (idTextView != null)
					idTextView.setText(String.valueOf(recipe.getId()));
				
				TextView nameTextView = (TextView) view.findViewById(R.id.checkable_name);
				if (nameTextView != null)
					nameTextView.setText(recipe.getName());
			}	
			
			return view;
		}
	}	
}
