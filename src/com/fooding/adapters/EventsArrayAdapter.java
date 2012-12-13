package com.fooding.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fooding.models.Event;

public class EventsArrayAdapter extends ArrayAdapter<Event> {
	private int resource;
	
	public EventsArrayAdapter(Context context, int resource, List<Event> events) {
		super(context, resource, events);
		
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout view;
		
		if (convertView == null) {
			view = new RelativeLayout(getContext());
			LayoutInflater inflater = 
					(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(resource, view);
		} else {
			view = (RelativeLayout) convertView;
		}
		
		Event event = getItem(position);
		
		RelativeLayout.LayoutParams leftParams = 
				new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_LEFT);
				
		TextView nameTextView = new TextView(getContext());
		nameTextView.setId(100);
		nameTextView.setText(event.getName());
		nameTextView.setLayoutParams(leftParams);
		
		RelativeLayout.LayoutParams rightParams =
				new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		TextView dateTextView = new TextView(getContext());
		dateTextView.setId(200);
		dateTextView.setText(event.getDateString());
		dateTextView.setLayoutParams(rightParams);
		
		view.addView(nameTextView);
		view.addView(dateTextView);		
		
		return view;
	}
}
