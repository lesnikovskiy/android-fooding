package com.fooding.models;

import java.util.Date;

public class Event {
	private long id;
	private String name;
	private Date date;
	
	public Event(String name, Date date) {
		this.name = name;
		this.date = date;
	}
	
	public Event(long id, String name, Date date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Date getDate() {
		return this.date;
	}
}
