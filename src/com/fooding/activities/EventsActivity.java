package com.fooding.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
							R.layout.event_item_layout, events);
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
	
	private class EventsArrayAdapter extends ArrayAdapter<Event> {			
		private Context context;
		private int resource;
		private List<Event> events;
		
		private View view;
		
		public EventsArrayAdapter(Context context, int resource, List<Event> events) {
			super(context, resource, events);
			
			this.context = context;
			this.resource = resource;
			this.events = events;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			view = convertView;
			
			if (view == null) {
				LayoutInflater inflater = 
						(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				this.view = inflater.inflate(resource, parent, false);
			}
			
			Event event = events.get(position);
			
			if (event == null)
				return view;
			
			TextView idTextView = (TextView) view.findViewById(R.id.item_id);
			if (idTextView != null)
				idTextView.setText(String.valueOf(event.getId()));
			
			TextView nameTextView = (TextView) view.findViewById(R.id.item_name);
			if (nameTextView != null)
				nameTextView.setText(event.getName());	
			
			return view;
		}
	}
}
