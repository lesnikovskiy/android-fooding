package com.fooding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.contracts.RecipesContract;
import com.fooding.models.Recipe;

public class InstructionsActivity extends Activity implements OnClickListener {
	static final private String TAG ="InstructionsActivity";
	static final private String RECIPE_ID = "recipe_id";
	
	//private TextView recipeIdTextView;
	private EditText instructionsEditText;
	
	private Button saveButton;
	private Button backButton;
	
	private RecipesContract recipesDB;
	private Recipe currentRecipe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions_layout);
		
		long recipeId = getIntent().getExtras().getLong(RECIPE_ID);
		
		recipesDB = new RecipeDbAdapter(getApplicationContext());
		recipesDB.open();
		
		currentRecipe = recipesDB.getById(recipeId);
		
		Log.d(TAG, String.format("Extracted recipe: [%s %s %s]", currentRecipe.getId(), 
				currentRecipe.getName(), currentRecipe.getInstructions()));
		
		if (currentRecipe == null) {
			Toast.makeText(getApplicationContext(), 
					"Problem occurred with passing the information to save the recipe", 
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		//recipeIdTextView = (TextView) findViewById(R.id.recipe_id_for_instructions);
		instructionsEditText = (EditText) findViewById(R.id.instructions_edit_text);
		instructionsEditText.setText(currentRecipe.getInstructions());
		
		saveButton = (Button) findViewById(R.id.instructions_save_button);
		backButton = (Button) findViewById(R.id.instructions_go_to_recipes_button);
		
		saveButton.setOnClickListener(this);
		backButton.setOnClickListener(this);		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recipesDB.close();
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.instructions_save_button:			
//			long _id = -1;
//			String recipeId = recipeIdTextView.getText().toString();
//			_id = Long.parseLong(recipeId);
//			Recipe recipe = recipesDB.getById(_id);
//			if (recipe != null) {
//				String instructions = instructionsEditText.getText().toString();
//				
//				Recipe udpRecipe = new Recipe(recipe.getId(), recipe.getName(), instructions);
//				boolean isUpdated = recipesDB.updateRecipe(udpRecipe);
//				
//				Log.d(TAG, String.format("Recipe update success: %s", isUpdated));
//				
//				if (isUpdated) {
//					Toast.makeText(getApplicationContext(), "Recipe successfully saved", Toast.LENGTH_LONG)
//							.show();
//				}
//			} else {
//				Log.d(TAG, "Something went wrong in save method");
//			}			
			
			if (currentRecipe == null)
				return;
			
			Recipe udpRecipe = new Recipe(currentRecipe.getId(), 
					currentRecipe.getName(), instructionsEditText.getText().toString());
			boolean isUpdated = recipesDB.updateRecipe(udpRecipe);
			
			Log.d(TAG, String.format("Recipe update status: %s", isUpdated));
			
			if (isUpdated) {
				Toast.makeText(getApplicationContext(), "Recipe successfully saved", Toast.LENGTH_LONG)
					.show();
			} else {
				Toast.makeText(getApplicationContext(), "Something bad happened! Recipe not saved", 
						Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.instructions_go_to_recipes_button:
			Intent intent = new Intent(getApplicationContext(), RecipesActivity.class);
			startActivity(intent);
			break;
		}
	}
}
