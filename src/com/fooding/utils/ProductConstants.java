package com.fooding.utils;

public final class ProductConstants {
	static final public String DB_NAME = "fooding.db";
	static final public int DB_VERSION = 1;
	
	static final public String PRODUCTS_TABLE = "products";
	
	static final public String PRODUCTID = "productid";
	static final public String NAME = "name";
	static final public String PRICE = "price";
	
	static final public int COLUMN_ID = 0;
	static final public int COLUMN_NAME = 1;
	static final public int COLUMN_PRICE = 2;
	
	static final public String ADD_FLAG = "ADD_FLAG";
	static final public String EDIT_FLAG = "EDIT_FLAG";
	static final public int PRODUCTS_RESULT = 100;
	
	static final public String CREATE_CMD = 
			"create table products" +
			"(productid integer primary key autoincrement, name text not null, price real);";
	static final public String DROP_CMD = "drop table if exists products;";
}
