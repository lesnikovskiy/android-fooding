package com.fooding.entities;

public class Product {
	private String id;
	private String rev;
	private String name;
	private String price;
	
	public Product(String id, String rev, String name, String price) {
		this.id = id;
		this.rev = rev;
		this.name = name;
		this.price = price;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getRev() {
		return this.rev;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPrice() {
		return this.price;
	}
}