package com.fooding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		Button productsButton = (Button) findViewById(R.id.products_button);
		productsButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.products_button:
				Intent intent = new Intent(v.getContext(), ProductsActivity.class);
				startActivity(intent);
				break;	
		}
	}
}
