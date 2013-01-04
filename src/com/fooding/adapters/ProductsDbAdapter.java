package com.fooding.adapters;

import static com.fooding.utils.DbConsts.DB_NAME;
import static com.fooding.utils.DbConsts.DB_VERSION;
import static com.fooding.utils.ProductConstants.COLUMN_ID;
import static com.fooding.utils.ProductConstants.COLUMN_NAME;
import static com.fooding.utils.ProductConstants.COLUMN_PRICE;
import static com.fooding.utils.ProductConstants.ID;
import static com.fooding.utils.ProductConstants.NAME;
import static com.fooding.utils.ProductConstants.PRICE;
import static com.fooding.utils.ProductConstants.PRODUCTS_TABLE;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.fooding.contracts.ProductDbContract;
import com.fooding.models.Product;

public class ProductsDbAdapter implements ProductDbContract {
	static final private String TAG = "ProductsDbAdapter";
	
	private final OpenHelper openHelper;
	private  SQLiteDatabase database;
	
	public ProductsDbAdapter(Context context) {
		openHelper = new OpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public void open() throws SQLiteException {
		try {
			database = openHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
			database = openHelper.getReadableDatabase();
		}
	}
	
	public void close() {
		database.close();
		openHelper.close();
	}
	
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		
		Cursor c = database.query(PRODUCTS_TABLE, 
				new String[] {ID, NAME, PRICE}, 
				null, null, null, null, null);
		
		if (c.moveToFirst() && c.getCount() > 0) {
			do {
				long productId = c.getLong(COLUMN_ID);
				String name = c.getString(COLUMN_NAME );
				double price = c.getDouble(COLUMN_PRICE);
				
				products.add(new Product(productId, name, price));
			} while (c.moveToNext());
		}
		
		return products;
	}
	
	public List<Product> getProductsByRecipes(long[] recipeIds) {
		List<Product> products = new ArrayList<Product>();
		List<String> stringIds = new ArrayList<String>();
		for (long id : recipeIds)
			stringIds.add(String.valueOf(id));
		
		String sql = String.format("select p.id _id, p.name, rp.product_quantity from products p " 
				+ "inner join recipe_products rp on rp.product_id = p.id " 
				+ "where rp.recipe_id in (%s)"
				+ "order by p.name;", TextUtils.join(",", stringIds));
		
		Cursor c = database.rawQuery(sql, null);
		if (c.moveToFirst()) {
			do {
				long id = c.getLong(0);
				String name = c.getString(1);
				String qty = c.getString(2);
				
				products.add(new Product(id, name, qty));
			} while (c.moveToNext());
		}
		
		return products;
	}
	
	public Cursor getProductFindCursor(String args) {
		String sqlQuery = "SELECT id _id, name FROM products WHERE name LIKE '%" + args + "%'";
		Cursor c = database.rawQuery(sqlQuery, null);
		
		return c;
	}

	public boolean insertProduct(Product product) {
		ContentValues values = new ContentValues();
		values.put(NAME, product.getName());
		values.put(PRICE, product.getPrice());
		
		return database.insert(PRODUCTS_TABLE, null, values) > 0;
	}

	public boolean updateProduct(Product product) {
		long productId = product.getId();
		if (productId <= 0) {
			Log.w(TAG, 
					String.format("Product was not updated as argument is less than or equals zero for '%s'",
							product.getName()));
			return insertProduct(product);
		}
		
		ContentValues values = new ContentValues();
		values.put(NAME, product.getName());
		values.put(PRICE, product.getPrice());
		
		String whereClause = String.format("%s=%s", ID, productId);
		
		return database.update(PRODUCTS_TABLE, values, whereClause, null) > 0;
	}

	public boolean deleteProduct(long productId) {
		Log.d(TAG, "deleteProduct called");
		Log.d(TAG, String.format("productId = %s", productId));
		if (productId <= 0)
			return false;
		
		String whereClause = String.format("%s=%s", ID, productId);
		Log.d(TAG, String.format("whereClause = %s", whereClause));
		
		return database.delete(PRODUCTS_TABLE, whereClause, null) > 0;
	}
	
	private static final String SQLITE_SEQUENCE = "sqlite_sequence";
	private static final String SELECTION = "name = ?";
	private static final String SEQ_COLUMN = "seq";
	
	public long getLastInsertId() throws IllegalArgumentException {
		long index = -1;
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		if (db == null)
			throw new NullPointerException("SQLiteDatabase is null. Override getDatabase() method correctly.");
		
		Cursor c = db.query(SQLITE_SEQUENCE, 
				new String[] {SEQ_COLUMN}, 
				SELECTION, 
				new String[] {PRODUCTS_TABLE}, 
				null, null, null);
		
		if (c.moveToFirst()) {
			index = c.getLong(c.getColumnIndexOrThrow(SEQ_COLUMN));
		}
		
		return index;
	}
}
