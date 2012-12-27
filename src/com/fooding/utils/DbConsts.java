package com.fooding.utils;

public abstract class DbConsts {
	static final public String DB_NAME = "fooding.db";
	static final public int DB_VERSION = 1;
	
	static final public String PRODUCTS_TABLE = "products";
	static final public String EVENTS_TABLE = "events";
	static final public String RECIPES_TABLE = "recipes";
	static final public String RECIPES_PRODUCTS = "recipe_products";
	
	static final public String CREATE_PRODUCTS_SQL = 
			"create table " + PRODUCTS_TABLE +
			"(id integer primary key autoincrement, name text not null, price real);";
	static final public String CREATE_EVENTS_SQL =
		    "create table " + EVENTS_TABLE +
			"(id integer primary key autoincrement, name text not null, date long);";
	static final public String CREATE_RECIPES_SQL = 
			"create table " + RECIPES_TABLE +
		    "(id integer primary key autoincrement, name text not null, instructions text);";
	static final public String CREATE_RECIPE_PRODUCTS_SQL = 
			"create table " + RECIPES_PRODUCTS +
			"(recipe_id integer not null, product_id integer not null, product_quantity text);";
	
	static final public String MIGRATE_PRODUCTS_SQL = 
			String.format("drop table if exists %s;", PRODUCTS_TABLE);
	static final public String MIGRATE_EVENTS_SQL = 
			String.format("drop table if exists %s;", EVENTS_TABLE);
	static final public String MIGRATE_RECIPES_SQL = 
			String.format("drop table if exists %s;", RECIPES_TABLE);
	static final public String MIGRATE_RECIPE_PRODUCTS_SQL = 
			String.format("drop table if exists %s;", CREATE_RECIPE_PRODUCTS_SQL);
}
