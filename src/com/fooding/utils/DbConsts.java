package com.fooding.utils;

public final class DbConsts {
	static final public String DB_NAME = "fooding.db";
	static final public int DB_VERSION = 1;
	
	static final public String PRODUCTS_TABLE = "products";
	
	static final public String PRODUCTID = "productid";
	static final public String ID = "id";
	static final public String REV = "rev";
	static final public String NAME = "name";
	static final public String PRICE = "price";
	
	static final public int COLUMN_PROD_ID = 0;
	static final public int COLUMN_ID = 1;
	static final public int COLUMN_REV = 2;
	static final public int COLUMN_NAME = 3;
	static final public int COLUMN_PRICE = 4;
	
	static final public String CREATE_CMD = 
			"create table products" +
			"(productid integer primary key autoincrement, id text, rev text, name text, price real);";
	static final public String DROP_CMD = "drop table if exists products;";
}
