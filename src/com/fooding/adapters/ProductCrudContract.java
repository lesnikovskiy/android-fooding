package com.fooding.adapters;

import java.util.List;

import com.fooding.entities.Product;

public interface ProductCrudContract {
	List<Product> getProducts();	
	boolean insertProduct(Product product);
	boolean updateProduct(Product product);
	boolean deleteProduct(int productId);	
}
