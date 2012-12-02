package com.fooding.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fooding.entities.Product;
import com.fooding.utils.DbConsts;

public class ProductsDbAdapter implements ProductCrudContract {
	static final private String TAG = "ProductsDbAdapter";
	
	private ProductsOpenHelper openHelper;
	private SQLiteDatabase database;
	
	public ProductsDbAdapter(Context context) {
		openHelper = new ProductsOpenHelper(context, DbConsts.DB_NAME, null, DbConsts.DB_VERSION);
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
		openHelper.close();
	}
	
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		
		Cursor c = database.query(DbConsts.DB_NAME, 
				new String[] {DbConsts.PRODUCTID, DbConsts.ID, 
					DbConsts.REV, DbConsts.NAME, DbConsts.PRICE}, 
				null, null, null, null, null);
		
		if (c.moveToFirst() && c.getCount() > 0) {
			do {
				long productId = c.getLong(DbConsts.COLUMN_PROD_ID);
				String id = c.getString(DbConsts.COLUMN_ID);
				String rev = c.getString(DbConsts.COLUMN_REV);
				String name = c.getString(DbConsts.COLUMN_NAME);
				String price = c.getString(DbConsts.COLUMN_PRICE);
				
				Product p = new Product(productId, id, rev, name, price);
				products.add(p);
			} while (c.moveToNext());
		}
		
		return products;
	}

	public boolean insertProduct(Product product) {
		ContentValues values = new ContentValues();
		values.put(DbConsts.ID, product.getId());
		values.put(DbConsts.REV, product.getRev());
		values.put(DbConsts.NAME, product.getName());
		values.put(DbConsts.PRICE, product.getPrice());
		
		return database.insert(DbConsts.DB_NAME, null, values) > 0;
	}

	public boolean updateProduct(Product product) {
		long productId = product.getProductId();
		if (productId <= 0)
			return insertProduct(product);
		
		ContentValues values = new ContentValues();
		values.put(DbConsts.ID, product.getId());
		values.put(DbConsts.REV, product.getRev());
		values.put(DbConsts.NAME, product.getName());
		values.put(DbConsts.PRICE, product.getPrice());
		
		String whereClause = String.format("%s=%s", DbConsts.PRODUCTID, productId);
		
		return database.update(DbConsts.DB_NAME, values, whereClause, null) > 0;
	}

	public boolean deleteProduct(int productId) {
		if (productId <= 0)
			return false;
		
		String whereClause = String.format("%s=%s", DbConsts.PRODUCTID, productId);
		
		return database.delete(DbConsts.DB_NAME, whereClause, null) > 0;
	}
	
	private static class ProductsOpenHelper extends SQLiteOpenHelper {
		static final private String TAG = "ProductsOpenHelper";
		
		public ProductsOpenHelper(Context context, String name,
				CursorFactory factory, int version) {		
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "executes sql command: " + DbConsts.CREATE_CMD);
			db.execSQL(DbConsts.CREATE_CMD);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "executes sql command: " + DbConsts.DROP_CMD);
			db.execSQL(DbConsts.DROP_CMD);
		}
		
	}
}
