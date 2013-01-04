package com.fooding.contracts;

import java.util.List;

import com.fooding.models.Recipe;

public interface RecipesContract extends DatabaseContract {
	List<Recipe> getRecipes();
	List<Recipe> getJoinRecipes();
	boolean insertRecipe(Recipe recipe);
	long getLastInsertId();
	boolean updateRecipe(Recipe recipe);	
	boolean deleteRecipe(long recipeId);
	
	int deleteRecipeBundle(String[] recipeIds);
	boolean addProductToRecipe(long productId, long recipeId, String qty);	
}
