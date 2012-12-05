package com.fooding.models;

public class Product {
	private long productid;
	private String name;
	private double price;
	
	public Product(String name, double price) {		
		this.name = name;
		this.price = price;
	}
	
	public Product(long productid, String name, double price) {
		this.productid = productid;
		this.name = name;
		this.price = price;
	}
	
	public long getProductId() {
		return this.productid;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	@Override
	public String toString() {
		return String.format("[productid: %s, name: %s, price: %10.2f]", 
				getProductId(), getName(), getPrice());
	}
}
