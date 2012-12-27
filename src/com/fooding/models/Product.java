package com.fooding.models;

public class Product {
	private long id;
	private String name;
	private double price;
	private String quantity;
	
	public Product(String name, double price) {		
		this.name = name;
		this.price = price;
	}
	
	public Product(long id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public Product(long id, String name, String quantity) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
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
	
	public String getQuantity() {
		return this.quantity;
	}
}
