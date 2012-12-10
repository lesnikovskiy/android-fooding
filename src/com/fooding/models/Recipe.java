package com.fooding.models;

import java.util.List;

public class Recipe {
	private long id;
	private String name;
	private List<Product> products;
	private String instructions;
	
	public Recipe(String name, String instructions) {
		this.name = name;
		this.instructions = instructions;
	}
	
	public Recipe(String name, String instructions, List<Product> products) {
		this.name = name;
		this.instructions = instructions;
		this.products = products;
	}
	
	public Recipe(long id, String name, String instructions, List<Product> products) {
		this.id = id;
		this.name = name;
		this.instructions = instructions;
		this.products = products;
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<Product> getProducts() {
		return this.products;
	}
	
	public String getInstructions() {
		return this.instructions;
	}
}
