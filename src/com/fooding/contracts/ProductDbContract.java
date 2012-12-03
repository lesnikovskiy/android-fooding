package com.fooding.contracts;

import java.util.List;

import com.fooding.entities.Product;

public interface ProductDbContract extends DatabaseContract {
	List<Product> getProducts();	
	boolean insertProduct(Product product);
	boolean updateProduct(Product product);
	boolean deleteProduct(long productId);	
}
