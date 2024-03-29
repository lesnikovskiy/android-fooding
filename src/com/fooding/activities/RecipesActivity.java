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
import android.widget.Toast;

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.contracts.RecipesContract;
import com.fooding.models.Recipe;

public class RecipesActivity extends Activity {	
	static final private String TAG = "RecipesActivity";
	
	static final private int DEFAULT_GROUP = 0;
	
	static final private int ADD_RECIPE_ITEM = 100;
	static final private int EDIT_RECIPE_ITEM = 110;
	static final private int DELETE_SELECTED_ITEM = 120;
	static final private int CREATE_LIST_ITEM = 130;
	
	static final private String SELECTED_RECIPES_KEY = "selectedRIDs";
	static final private String RECIPE_ID = "recipe_id";
	
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
		menu.add(DEFAULT_GROUP, ADD_RECIPE_ITEM, Menu.NONE, R.string.recipes_add_recipe_menu_item)
			.setShortcut('0', 'a')
			.setIcon(R.drawable.new_mdpi)
			.setIntent(new Intent(getApplicationContext(), AddRecipeActivity.class));
		
		menu.add(DEFAULT_GROUP, EDIT_RECIPE_ITEM, Menu.NONE, R.string.recipes_edit_recipe_menu_item)
			.setIcon(R.drawable.edit_mdpi)
			.setShortcut('2', 'e');
		
		menu.add(DEFAULT_GROUP, DELETE_SELECTED_ITEM, Menu.NONE, R.string.recipes_delete_selected_menu_item)
			.setIcon(R.drawable.delete_mdpi)
			.setShortcut('3', 'd');		
		
		menu.add(DEFAULT_GROUP, CREATE_LIST_ITEM, Menu.NONE, R.string.recipes_create_list_menu_item)
			.setIcon(R.drawable.search_mdpi)
			.setShortcut('1', 'c');
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case EDIT_RECIPE_ITEM:
			List<Recipe> selectedRecipesForEdit = getSelectedRecipes();
			if (selectedRecipesForEdit.size() > 0) {
				Recipe recipe = selectedRecipesForEdit.get(0);
				Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
				intent.putExtra(RECIPE_ID, String.valueOf(recipe.getId()));
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "No recipes selected", Toast.LENGTH_LONG)
					.show();
			}
			break;
		case DELETE_SELECTED_ITEM: 
			List<Recipe> recipesToDelete = getSelectedRecipes();
			for (Recipe r : recipesToDelete) {
				if (r.getId() > 0) {
					db.deleteRecipe(r.getId());					
					adapter.remove(r);	
					recipesListView.setItemChecked(r.getPosition(), false);
					
					Log.d(TAG, String.format("Recipe deleted: [%s %s]", r.getId(), r.getName()));
				} else {
					Log.e(TAG, String.format("Recipe id = %s was not deleted", r.getId()));
				}
			}
			
			adapter.notifyDataSetChanged();
			break;
		case CREATE_LIST_ITEM:
			List<Recipe> selectedRecipes = getSelectedRecipes();	
			long[] selectedRIDs = new long [selectedRecipes.size()];
			for (int i = 0; i < selectedRecipes.size(); i++) {
				selectedRIDs[i] = selectedRecipes.get(i).getId();
			}
			
			if (selectedRIDs.length > 0) {
				Intent intent = new Intent(getApplicationContext(), PurchaseListAcitivity.class);
				intent.putExtra(SELECTED_RECIPES_KEY, selectedRIDs);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "No items selected", Toast.LENGTH_LONG).show();
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
	
	private List<Recipe> getSelectedRecipes() {
		List<Recipe> recipes = new ArrayList<Recipe>();
		
		final SparseBooleanArray checkedItems = recipesListView.getCheckedItemPositions();
		for (int i = 0; i < checkedItems.size(); i++) {			
			int position = checkedItems.keyAt(i);			
			if (checkedItems.valueAt(i)) {
				Recipe recipe = adapter.getItem(position);
				recipe.setPosition(position);
				recipes.add(recipe);
			}
		}
		
		return recipes;
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
