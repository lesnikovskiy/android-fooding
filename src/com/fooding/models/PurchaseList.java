package com.fooding.models;

import java.util.List;

public class PurchaseList {
	private long id;
	private List<Product> products;
	
	public PurchaseList(List<Product> products) {
		this.products = products;
	}
	
	public PurchaseList(long id, List<Product> products) {
		this.id = id;
		this.products = products;
	}
	
	public long getId() {
		return this.id;
	}
	
	public List<Product> getProducts() {
		return products;
	}
}
