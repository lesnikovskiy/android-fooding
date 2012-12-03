package com.fooding.adapters;

import static com.fooding.utils.DbConsts.COLUMN_ID;
import static com.fooding.utils.DbConsts.COLUMN_NAME;
import static com.fooding.utils.DbConsts.COLUMN_PRICE;
import static com.fooding.utils.DbConsts.COLUMN_PROD_ID;
import static com.fooding.utils.DbConsts.COLUMN_REV;
import static com.fooding.utils.DbConsts.CREATE_CMD;
import static com.fooding.utils.DbConsts.DB_NAME;
import static com.fooding.utils.DbConsts.DB_VERSION;
import static com.fooding.utils.DbConsts.DROP_CMD;
import static com.fooding.utils.DbConsts.ID;
import static com.fooding.utils.DbConsts.NAME;
import static com.fooding.utils.DbConsts.PRICE;
import static com.fooding.utils.DbConsts.PRODUCTID;
import static com.fooding.utils.DbConsts.PRODUCTS_TABLE;
import static com.fooding.utils.DbConsts.REV;

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

import com.fooding.contracts.ProductDbContract;
import com.fooding.entities.Product;

public class ProductsDbAdapter implements ProductDbContract {
	static final private String TAG = "ProductsDbAdapter";
	
	private ProductsOpenHelper openHelper;
	private SQLiteDatabase database;
	
	public ProductsDbAdapter(Context context) {
		openHelper = new ProductsOpenHelper(context, DB_NAME, null, DB_VERSION);
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
		
		Cursor c = database.query(PRODUCTS_TABLE, 
				new String[] {PRODUCTID, ID, REV, NAME, PRICE}, 
				null, null, null, null, null);
		
		if (c.moveToFirst() && c.getCount() > 0) {
			do {
				long productId = c.getLong(COLUMN_PROD_ID);
				String id = c.getString(COLUMN_ID);
				String rev = c.getString(COLUMN_REV);
				String name = c.getString(COLUMN_NAME);
				String price = c.getString(COLUMN_PRICE);
				
				Product p = new Product(productId, id, rev, name, price);
				products.add(p);
			} while (c.moveToNext());
		}
		
		return products;
	}

	public boolean insertProduct(Product product) {
		ContentValues values = new ContentValues();
		values.put(ID, product.getId());
		values.put(REV, product.getRev());
		values.put(NAME, product.getName());
		values.put(PRICE, product.getPrice());
		
		return database.insert(PRODUCTS_TABLE, null, values) > 0;
	}

	public boolean updateProduct(Product product) {
		long productId = product.getProductId();
		if (productId <= 0)
			return insertProduct(product);
		
		ContentValues values = new ContentValues();
		values.put(ID, product.getId());
		values.put(REV, product.getRev());
		values.put(NAME, product.getName());
		values.put(PRICE, product.getPrice());
		
		String whereClause = String.format("%s=%s", PRODUCTID, productId);
		
		return database.update(PRODUCTS_TABLE, values, whereClause, null) > 0;
	}

	public boolean deleteProduct(long productId) {
		Log.d(TAG, "deleteProduct called");
		Log.d(TAG, String.format("productId = %s", productId));
		if (productId <= 0)
			return false;
		
		String whereClause = String.format("%s=%s", PRODUCTID, productId);
		Log.d(TAG, String.format("whereClause = %s", whereClause));
		
		return database.delete(PRODUCTS_TABLE, whereClause, null) > 0;
	}
	
	private static class ProductsOpenHelper extends SQLiteOpenHelper {
		static final private String TAG = "ProductsOpenHelper";
		
		public ProductsOpenHelper(Context context, String name,
				CursorFactory factory, int version) {		
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "executes sql command: " + CREATE_CMD);
			db.execSQL(CREATE_CMD);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "executes sql command: " + DROP_CMD);
			db.execSQL(DROP_CMD);
		}		
	}
}
