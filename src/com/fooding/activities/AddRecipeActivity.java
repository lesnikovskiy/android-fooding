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

import com.fooding.adapters.RecipeDbAdapter;
import com.fooding.contracts.RecipesContract;
import com.fooding.models.Recipe;

public class AddRecipeActivity extends Activity implements OnClickListener {
	static final private String TAG = "AddRecipeActivity";	
	static final private String RECIPE_ID_KEY = "recipeId";
	static final private String RECIPE_NAME_KEY = "recipeName";
	
	static final private String INCOMING_RECIPE_ID = "recipe_id";
	static final private String INCOMING_RECIPE_NAME = "recipe_name";
	
	private TextView recipeIdText;
	private EditText recipeNameText;
	private RecipesContract db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe_layout);
		
		String incomingRecipeId = getIntent().getExtras() != null
				? getIntent().getExtras().getString(INCOMING_RECIPE_ID)
			    : "";
		String incomingRecipeName = getIntent().getExtras() != null
				? getIntent().getExtras().getString(INCOMING_RECIPE_NAME)
				: "";
		
		db = new RecipeDbAdapter(getApplicationContext());
		db.open();
		
		recipeIdText = (TextView) findViewById(R.id.incoming_recipe_id_text);
		recipeNameText = (EditText) findViewById(R.id.recipe_name);
		
		if (!TextUtils.isEmpty(incomingRecipeId) 
					&& TextUtils.isDigitsOnly(incomingRecipeId)) {
			recipeIdText.setText(incomingRecipeId);
		}
		
		if (!TextUtils.isEmpty(incomingRecipeName)) {
			recipeNameText.setText(incomingRecipeName);
		}
		
		final Button nextBtn = (Button) findViewById(R.id.next_button);
		nextBtn.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.next_button:
			long _id = -1;
			
			String recipeId = recipeIdText.getText().toString();
			if (!TextUtils.isEmpty(recipeId) && TextUtils.isDigitsOnly(recipeId)) {
				try {
					_id = Long.parseLong(recipeId);
				} catch (NumberFormatException e) {
					Log.e(TAG, 
							String.format("NumberFormatException is thrown with error message: %s", 
									e.getMessage()));
				}
			}
			
			String recipeName = recipeNameText.getText().toString();
			boolean isInserted = false;
			
			if (_id > 0) {
				boolean isUpdated = db.updateRecipe(new Recipe(_id, recipeName, ""));
				Log.d(TAG, String.format("Recipe update success: %s", isUpdated));
			} else {
				isInserted = db.insertRecipe(new Recipe(recipeName, ""));
				Log.d(TAG, String.format("Recipe insert success: %s", isInserted));
			}
			
			long outputRecipeId = isInserted ? db.getLastInsertId() : _id;
			Log.w(TAG, String.format("Retrieved last recipe inserted id: %s", outputRecipeId));
			
			Intent intent = new Intent(getApplicationContext(), SelectProductActivity.class);
			intent.putExtra(RECIPE_ID_KEY, outputRecipeId);
			intent.putExtra(RECIPE_NAME_KEY, recipeName);
			startActivity(intent);
			
			break;
		}
	}
}
