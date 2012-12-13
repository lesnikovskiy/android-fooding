package com.fooding.activities;

import java.util.Date;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fooding.adapters.EventsDbAdapter;
import com.fooding.models.Event;

public class AddEventActivity extends Activity implements OnClickListener {
	private static final String TAG = "AddEventActivity";
	
	private EventsDbAdapter db;
	private TextView nameTextView;
	private DatePicker dp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_layout);
		
		db = new EventsDbAdapter(getApplicationContext());
		
		try {			
			db.open();
		} catch (SQLiteException e) {
			Log.e(TAG, String.format("SQLiteException thrown: %s", e.getMessage()));
		}
		
		nameTextView = (TextView) findViewById(R.id.event_name);
		dp = (DatePicker) findViewById(R.id.events_date_picker);
		
		final Button addButton = (Button) findViewById(R.id.save_event_button);
		addButton.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.save_event_button:
			Log.d(TAG, "saving event to database");			
			String name = nameTextView.getText().toString();
			Log.d(TAG, String.format("saving event to database: %s", name));
			
			Log.d(TAG, "dp.getYear() = " + dp.getYear());
			Log.d(TAG, "dp.getMonth() = " + dp.getMonth());
			Log.d(TAG, "dp.getDayOfMonth() = " + dp.getDayOfMonth());
			Date date = new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
			
			db.insertEvent(new Event(name, date));
			break;
		}
	}
}
