package com.fooding.adapters;

import static com.fooding.utils.DbConsts.DB_NAME;
import static com.fooding.utils.DbConsts.DB_VERSION;
import static com.fooding.utils.DbConsts.PRODUCTS_TABLE;
import static com.fooding.utils.DbConsts.RECIPES_PRODUCTS;
import static com.fooding.utils.DbConsts.RECIPES_TABLE;
import static com.fooding.utils.RecipeConsts.COLUMN_ID;
import static com.fooding.utils.RecipeConsts.COLUMN_INSTRUCTIONS;
import static com.fooding.utils.RecipeConsts.COLUMN_NAME;
import static com.fooding.utils.RecipeConsts.COLUMN_PRODUCT_ID;
import static com.fooding.utils.RecipeConsts.COLUMN_P_ID;
import static com.fooding.utils.RecipeConsts.COLUMN_P_NAME;
import static com.fooding.utils.RecipeConsts.COLUMN_P_PRICE;
import static com.fooding.utils.RecipeConsts.PRODUCT_ID;
import static com.fooding.utils.RecipeConsts.P_ID;
import static com.fooding.utils.RecipeConsts.P_NAME;
import static com.fooding.utils.RecipeConsts.P_PRICE;
import static com.fooding.utils.RecipeConsts.RECIPE_ID;
import static com.fooding.utils.RecipeConsts.R_ID;
import static com.fooding.utils.RecipeConsts.R_INSTRUCTIONS;
import static com.fooding.utils.RecipeConsts.R_NAME;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.fooding.contracts.RecipesContract;
import com.fooding.models.Product;
import com.fooding.models.Recipe;

public class RecipeDbAdapter implements RecipesContract {
	final static private String TAG = "RecipeDbAdapter";
	
	private static final String SQLITE_SEQUENCE = "sqlite_sequence";
	private static final String SELECTION = "name = ?";
	private static final String SEQ = "seq";
	
	private final OpenHelper openHelper;
	private SQLiteDatabase db;
	
	public RecipeDbAdapter(Context context) {
		openHelper = new OpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public void open() throws SQLiteException {
		try {
			db = openHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			Log.e(TAG, String.format("open() thrown SQLiteException: %s", e.getMessage()));
			Log.d(TAG, "Trying to get readable database");
			db = openHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}
	
	public List<Recipe> getRecipes() {
		List<Recipe> recipes = new ArrayList<Recipe>();
		
		Cursor c = db.query(RECIPES_TABLE, new String[] {R_ID, R_NAME}, 
				null, null, null, null, null);
		
		if (c.moveToFirst() && c.getCount() > 0) {
			do {
				long id = c.getLong(COLUMN_ID);
				String name = c.getString(COLUMN_NAME);
				
				recipes.add(new Recipe(id, name));
			} while (c.moveToNext());
		}
		
		return recipes;
	}

	public List<Recipe> getJoinRecipes() {
		List<Recipe> recipes = new ArrayList<Recipe>();
		
		Cursor c = db.query(RECIPES_TABLE, new String[] {R_ID, R_NAME, R_INSTRUCTIONS}, 
				null, null, null, null, null);
		
		if (c.moveToFirst() && c.getCount() > 0) {
			do {
				long id = c.getLong(COLUMN_ID);
				String name = c.getString(COLUMN_NAME);
				String instructions = c.getString(COLUMN_INSTRUCTIONS);
				List<Product> products = new ArrayList<Product>();
				
				List<String> productIds = new ArrayList<String>();
				Cursor pc = db.query(RECIPES_PRODUCTS, new String[] {RECIPE_ID, PRODUCT_ID}, 
						String.format("%s = ", RECIPE_ID), new String[] {String.valueOf(id)}, 
						null, null, null);
				
				if (pc.moveToFirst() && pc.getCount() > 0) {
					do {
						String productId = pc.getString(COLUMN_PRODUCT_ID);
						productIds.add(productId);
					} while (pc.moveToNext());
				}
				
				if (productIds.size() > 0) {
					Cursor prc = db.query(PRODUCTS_TABLE, 
							new String[] {P_ID, P_NAME, P_PRICE}, 
							String.format("%s in (?)"), productIds.toArray(new String[productIds.size()]), 
							null, null, null);
					
					if (prc.moveToFirst() && prc.getCount() > 0) {
						do {
							long pid = prc.getLong(COLUMN_P_ID);
							String pname = prc.getString(COLUMN_P_NAME);
							double price = prc.getDouble(COLUMN_P_PRICE);
							
							products.add(new Product(pid, pname, price));
						} while (prc.moveToNext());
					}
				}
				
				recipes.add(new Recipe(id, name, instructions, products));
			} while (c.moveToNext());
		}
		
		return recipes;
	}

	public boolean insertRecipe(Recipe recipe) {
		ContentValues values = new ContentValues();
		values.put(R_NAME, recipe.getName());
		values.put(R_INSTRUCTIONS, recipe.getInstructions());
		
		return db.insert(RECIPES_TABLE, null, values) > 0;
	}
	
	public long getLastInsertId() {
		long index = -1;
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		if (db == null)
			throw new NullPointerException("SQLiteDatabase is null. Override getDatabase() method correctly.");
		
		Cursor c = db.query(SQLITE_SEQUENCE, 
				new String[] {SEQ}, 
				SELECTION, 
				new String[] {RECIPES_TABLE}, null, null, null);
		if (c.moveToFirst()) {
			index = c.getLong(c.getColumnIndex(SEQ));
		}
		
		return index;
	}

	public boolean updateRecipe(Recipe recipe) {
		ContentValues values = new ContentValues();
		values.put(R_NAME, recipe.getName());
		values.put(R_INSTRUCTIONS, recipe.getInstructions());
		
		String whereClause = String.format("%s = %s", R_ID, recipe.getId());
		
		return db.update(RECIPES_TABLE, values, whereClause , null) > 0;
	}

	public boolean deleteRecipe(long recipeId) {
		String whereClause = String.format("%s = %s", R_ID, recipeId);
		
		return db.delete(RECIPES_TABLE, whereClause, null) > 0;
	}

	public boolean addProductToRecipe(long productId, long recipeId) {
		ContentValues values = new ContentValues();
		values.put(RECIPE_ID, recipeId);
		values.put(PRODUCT_ID, productId);
		
		return db.insert(RECIPES_PRODUCTS, null, values) > 0;
	}
}
