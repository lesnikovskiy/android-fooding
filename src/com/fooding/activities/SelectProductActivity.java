package com.fooding.activities;

import android.app.Activity;
import android.os.Bundle;

public class SelectProductActivity extends Activity {
	static final private String TAG = "SelectProductActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_product_layout);
	}
}
