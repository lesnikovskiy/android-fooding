package com.fooding.contracts;

import java.util.List;

import android.database.Cursor;

import com.fooding.models.Product;

public interface ProductDbContract extends DatabaseContract {
	List<Product> getProducts();
	Cursor getProductFindCursor(String args);
	boolean insertProduct(Product product);
	boolean updateProduct(Product product);
	boolean deleteProduct(long productId);	
	long getLastInsertId() throws IllegalArgumentException;
}
