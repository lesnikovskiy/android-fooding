package com.fooding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

public class MainActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main_layout);
		
		TableLayout ll = (TableLayout) findViewById(R.id.main_table_layout);
		ll.setBackgroundResource(R.drawable.fooding);
		
		final Button eventListButton = (Button) findViewById(R.id.event_list_button);
		eventListButton.setOnClickListener(this);
		final Button recipesButton = (Button) findViewById(R.id.recipes_button);
		recipesButton.setOnClickListener(this);
		final Button purchaseListButton = (Button) findViewById(R.id.purchase_list_button);
		purchaseListButton.setOnClickListener(this);
		final Button availableProductsButton = (Button) findViewById(R.id.available_products_button);
		availableProductsButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.event_list_button:
			Intent eventsActivity = new Intent(v.getContext(), EventsActivity.class);
			startActivity(eventsActivity);
			break;
			
		case R.id.purchase_list_button:
			Intent prodsActivity = new Intent(v.getContext(), ProductsActivity.class);
			startActivity(prodsActivity);
			break;
		}
	}
}
