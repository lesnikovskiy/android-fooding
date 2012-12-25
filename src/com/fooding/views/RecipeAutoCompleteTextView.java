package com.fooding.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class RecipeAutoCompleteTextView extends AutoCompleteTextView {

	public RecipeAutoCompleteTextView(Context context) {
		super(context);
	}

	public RecipeAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public RecipeAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void replaceText(CharSequence text) {
		// TODO Auto-generated method stub
		super.replaceText(text);
	}
	
	@Override
	protected void performFiltering(CharSequence text, int keyCode) {
		// TODO Auto-generated method stub
		super.performFiltering(text, keyCode);
	}
}
