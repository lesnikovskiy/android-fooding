package com.fooding.entities;

public class Product {
	private long productid;
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
	
	public Product(long productid, String id, String rev, String name, String price) {
		this.productid = productid;
		this.id = id;
		this.rev = rev;
		this.name = name;
		this.price = price;
	}
	
	public long getProductId() {
		return this.productid;
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
	
	@Override
	public String toString() {
		return String.format("[productid: %s, id: %s, rev: %s, name: %s, price: %s]", 
				getProductId(), getId(), getRev(), getName(), getPrice());
	}
}
