package com.fooding.utils;

public abstract class DbConsts {
	static final public String DB_NAME = "fooding.db";
	static final public int DB_VERSION = 1;
	static final public String CREATE_PRODUCTS_SQL = 
			"create table products" +
			"(id integer primary key autoincrement, name text not null, price real);";
	static final public String CREATE_EVENTS_SQL =
		    "create table events" +
			"(id integer primary key autoincrement, name text not null, date long);";
	
	static final public String MIGRATE_PRODUCTS_SQL = 
			"drop table if exists products;";
	static final public String MIGRATE_EVENTS_SQL = 
			"drop table if exists events;";
}
