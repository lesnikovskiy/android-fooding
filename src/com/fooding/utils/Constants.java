package com.fooding.utils;

public final class Constants {
	public final static String ID = "id";
	public final static String REV = "rev";
	public final static String NAME = "name";
	public final static String PRICE = "price";
	
	/* 
	 * I will follow next convention:
	 * to get list + /api/item/list
	 * to update call put /api/item
	 * to insert call post /api/item
	 * to delete call delete /api/item/:id/:rev
	 *   
	 *  */
	public final static String HOST = "http://fooding.jit.su";
	public final static String API_LIST_URL = "/list";
	public final static String API_PRODUCT_URL = "/api/product";	
	
	public final static String EDIT_FLAG = "edit_flag";
	public final static String ADD_FLAG = "add_flag";
	
	public final static int EDIT_PRODUCTS_RESULT = 100;
	public final static int ADD_PRODUCTS_RESULT = 200;
	
	public final static String buildListUrl(String host, String entity, String list) {
		return String.format("%s%s%s", host, entity, list);
	}
	
	public final static String buildEntityUrl(String host, String entity) {
		return String.format("%s%s", host, entity);
	}
	
	public final static String buildDeleteUrl(String host, String entity, String ... args) {		
		String url = buildEntityUrl(host, entity);
		StringBuffer buffer = new StringBuffer(url);
		for (String arg : args)
			buffer.append("/").append(arg);
		
		return buffer.toString();
	}
}
