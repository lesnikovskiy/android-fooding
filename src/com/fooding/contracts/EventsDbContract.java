package com.fooding.contracts;

import java.util.List;

import com.fooding.models.Event;

public interface EventsDbContract extends DatabaseContract {
	List<Event> getEvents();
	boolean insertEvent(Event event);
	boolean updateEvent(Event event);
	boolean deleteEvent(long id);
}
