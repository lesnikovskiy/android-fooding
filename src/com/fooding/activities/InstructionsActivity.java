package com.fooding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.contracts.RecipesContract;
import com.fooding.models.Recipe;

public class InstructionsActivity extends Activity implements OnClickListener {
	static final private String TAG ="InstructionsActivity";
	static final private String RECIPE_ID = "recipe_id";
	
	private TextView recipeIdTextView;
	private EditText instructionsEditText;
	
	private Button saveButton;
	private Button backButton;
	
	private RecipesContract recipesDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions_layout);
		
		String recipeId = getIntent().getExtras().getString(RECIPE_ID);
		if (TextUtils.isEmpty(recipeId) && !TextUtils.isDigitsOnly(recipeId)) {
			Toast.makeText(getApplicationContext(), 
					"Problem occurred with passing the information to save the recipe", 
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		recipeIdTextView = (TextView) findViewById(R.id.recipe_id_for_instructions);
		recipeIdTextView.setText(recipeId);
		instructionsEditText = (EditText) findViewById(R.id.instructions_edit_text);
		
		saveButton = (Button) findViewById(R.id.instructions_save_button);
		backButton = (Button) findViewById(R.id.instructions_go_to_recipes_button);
		
		saveButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		
		recipesDB = new RecipeDbAdapter(getApplicationContext());
		recipesDB.open();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recipesDB.close();
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.instructions_save_button:			
			long _id = -1;
			String recipeId = recipeIdTextView.getText().toString();
			_id = Long.parseLong(recipeId);
			Recipe recipe = recipesDB.getById(_id);
			if (recipe != null) {
				String instructions = instructionsEditText.getText().toString();
				
				Recipe udpRecipe = new Recipe(recipe.getId(), recipe.getName(), instructions);
				boolean isUpdated = recipesDB.updateRecipe(udpRecipe);
				
				Log.d(TAG, String.format("Recipe update success: %s", isUpdated));
				
				if (isUpdated) {
					Toast.makeText(getApplicationContext(), "Recipe successfully saved", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Log.d(TAG, "Something went wrong in save method");
			}			
			
			break;
		case R.id.instructions_go_to_recipes_button:
			Intent intent = new Intent(getApplicationContext(), RecipesActivity.class);
			startActivity(intent);
			break;
		}
	}
}
