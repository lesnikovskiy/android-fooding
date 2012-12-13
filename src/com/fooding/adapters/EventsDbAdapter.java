package com.fooding.adapters;

import static com.fooding.utils.DbConsts.DB_NAME;
import static com.fooding.utils.DbConsts.DB_VERSION;
import static com.fooding.utils.EventsConsts.COLUMN_DATE;
import static com.fooding.utils.EventsConsts.COLUMN_ID;
import static com.fooding.utils.EventsConsts.COLUMN_NAME;
import static com.fooding.utils.EventsConsts.DATE;
import static com.fooding.utils.EventsConsts.EVENTS_TABLE;
import static com.fooding.utils.EventsConsts.ID;
import static com.fooding.utils.EventsConsts.NAME;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.fooding.contracts.EventsDbContract;
import com.fooding.models.Event;

public class EventsDbAdapter implements EventsDbContract {
	static final private String TAG = "EventsDbAdapter";
	
	private final OpenHelper openHelper;
	private SQLiteDatabase db;
	
	public EventsDbAdapter(Context context) {
		openHelper = new OpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public void open() throws SQLiteException {
		try {
			db = openHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			Log.e(TAG, String.format("SQLiteException thrown: %s", e.getMessage()));
			db = openHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
		openHelper.close();
	}
	
	public List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();
		
		Cursor c = db.query(EVENTS_TABLE, 
				new String[] {ID, NAME, DATE}, 
				null, null, null, null, null);
		
		if (c.moveToFirst() && c.getCount() > 0) {
			do {
				long id = c.getLong(COLUMN_ID);
				String name = c.getString(COLUMN_NAME);
				Date date = new Date();
				try {
					long d = c.getLong(COLUMN_DATE);
					date = new Date(d);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				
				events.add(new Event(id, name, date));				
			} while (c.moveToNext());
		}
		
		return events;
	}

	public boolean insertEvent(Event event) {
		ContentValues values = new ContentValues();
		values.put(NAME, event.getName());
		values.put(DATE, event.getDate().getTime());
		
		return db.insert(EVENTS_TABLE, null, values) > 0;
	}

	public boolean updateEvent(Event event) {
		ContentValues values = new ContentValues();
		values.put(NAME, event.getName());
		values.put(DATE, event.getDate().getTime());
		
		String whereClause = String.format("%s=%s", ID, event.getId());
		
		return db.update(EVENTS_TABLE, values, whereClause, null) > 0;
	}

	public boolean deleteEvent(long id) {
		String whereClause = String.format("%s=%s", ID, id);
		
		return db.delete(EVENTS_TABLE, whereClause, null) > 0;
	}	
}
