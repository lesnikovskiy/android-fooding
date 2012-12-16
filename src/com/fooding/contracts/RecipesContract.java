package com.fooding.contracts;

import java.util.List;

import com.fooding.models.Recipe;

public interface RecipesContract extends DatabaseContract {
	List<Recipe> getRecipes();
	List<Recipe> getJoinRecipes();
	boolean insertRecipe(Recipe recipe);
	boolean updateRecipe(Recipe recipe);
	boolean deleteRecipe(long recipeId);
	boolean addProductToRecipe(long productId, long recipeId);	
}
