package com.fooding.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.models.Recipe;

public class AddRecipeActivity extends Activity implements OnClickListener {
	static final private String TAG = "AddRecipeActivity";
	
	private EditText recipeNameText;
	private RecipeDbAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe_layout);
		
		db = new RecipeDbAdapter(getApplicationContext());
		db.open();
		
		recipeNameText = (EditText) findViewById(R.id.recipe_name);
		final Button nextBtn = (Button) findViewById(R.id.next_button);
		nextBtn.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.next_button:
			String recipeName = recipeNameText.getText().toString();
			String instructions = getResources().getString(R.string.recipes_todo_add_instructions_text);
			if (db.insertRecipe(new Recipe(recipeName, instructions))) {
				Log.w(TAG, "recipe name saved!");
			} else {
				Log.e(TAG, "recipe wasn't saved!");
			}
			break;
		}
	}
}
