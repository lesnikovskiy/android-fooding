package com.fooding.models;

import java.util.List;

public class AvailableList {
	private long id;
	private List<Product> products;
	
	public AvailableList(List<Product> products) {
		this.products = products;
	}
	
	public AvailableList(long id, List<Product> products) {
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
