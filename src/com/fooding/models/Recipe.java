package com.fooding.models;

import java.util.List;

public class Recipe {
	private long id;
	private String name;
	private String instructions;
	private List<Product> products;	
	private int position;
	
	public Recipe(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Recipe(String name, String instructions) {
		this.name = name;
		this.instructions = instructions;
	}
	
	public Recipe(String name, String instructions, List<Product> products) {
		this.name = name;
		this.instructions = instructions;
		for (Product p : products) {
			this.addProduct(p);
		}
	}
	
	public Recipe(long id, String name, String instructions, List<Product> products) {
		this.id = id;
		this.name = name;
		this.instructions = instructions;
		for (Product p : products) {
			this.products.add(p);
		}
	}	
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getInstructions() {
		return this.instructions;
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public List<Product> getProducts() {
		return this.products;
	}	
	
	public int getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
}
