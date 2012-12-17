package com.fooding.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.models.Recipe;

public class RecipesActivity extends ListActivity {
	static final private String TAG = "RecipesActivity";
	static final private int MENU_ADD = 100;
	
	private RecipeDbAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView listView = (ListView) getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		db = new RecipeDbAdapter(getApplicationContext());
		db.open();
		
		List<Recipe> recipes = db.getRecipes();
		
		if (recipes.size() > 0) {
			RecipeArrayAdapter adapter = 
					new RecipeArrayAdapter(getApplicationContext(), 
							android.R.layout.simple_list_item_multiple_choice, recipes);
			setListAdapter(adapter);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		MenuItem addItem = menu.add(0, MENU_ADD, Menu.NONE, R.string.recipes_add_recipe_menu_item);
		addItem.setShortcut('0', 'a');
		addItem.setIntent(new Intent(getApplicationContext(), AddRecipeActivity.class));
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
	private class RecipeArrayAdapter extends ArrayAdapter<Recipe> {
		private int resource;
		
		public RecipeArrayAdapter(Context context, int resource, List<Recipe> recipes) {
			super(context, resource, recipes);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout view;			
			
			if (convertView == null) {
				view = new LinearLayout(getContext());
				LayoutInflater inflater = 
						(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater.inflate(resource, view);
			} else {
				view = (LinearLayout) convertView;
			}
			
			Recipe recipe = getItem(position);
			
			TextView idTV = new TextView(getContext());
			idTV.setVisibility(View.GONE);
			idTV.setText(String.valueOf(recipe.getId()));
			view.addView(idTV);
			
			TextView tv = new TextView(getContext());
			tv.setText(recipe.getName());
			
			LinearLayout.LayoutParams params =
					new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 25;
			
			tv.setLayoutParams(params);
			
			view.addView(tv);
			
			return view;
		}
	}
}
