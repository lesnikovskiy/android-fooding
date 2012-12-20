package com.fooding.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fooding.adapters.EventsArrayAdapter;
import com.fooding.adapters.EventsDbAdapter;
import com.fooding.contracts.EventsDbContract;
import com.fooding.models.Event;

public class EventsActivity extends ListActivity {
	private final static String TAG = "EventsActivity";
	
	private final static int MENU_ADD = 100;
	
	private EventsDbContract dbAdapter;
	private EventsArrayAdapter arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbAdapter = new EventsDbAdapter(getApplicationContext());
		
		try {
			dbAdapter.open();
		} catch (SQLiteException e) {
			Log.e(TAG, String.format("SQLiteException: %s", e.getMessage()));
		}
		
		List<Event> events = dbAdapter.getEvents();		
		
		if (events.size() > 0) {
			arrayAdapter = 
					new EventsArrayAdapter(getApplicationContext(), 
							android.R.layout.simple_list_item_1, events);
			setListAdapter(arrayAdapter);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem addItem = menu.add(0, MENU_ADD, Menu.NONE, R.string.events_add_menu_item);
		addItem.setShortcut('0', 'a');
		addItem.setIcon(R.drawable.cal);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
		case MENU_ADD:
			Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
			startActivity(intent);
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onDestroy() {
		dbAdapter.close();
		super.onDestroy();
	}
}
