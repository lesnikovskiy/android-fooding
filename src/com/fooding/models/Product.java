package com.fooding.models;

public class Product {
	private long id;
	private String name;
	private double price;
	
	public Product(String name, double price) {		
		this.name = name;
		this.price = price;
	}
	
	public Product(long productid, String name, double price) {
		this.id = productid;
		this.name = name;
		this.price = price;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getPrice() {
		return this.price;
	}
}
