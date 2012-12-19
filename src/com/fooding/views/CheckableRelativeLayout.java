package com.fooding.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
	private boolean isChecked;
	private List<Checkable> checkableViews;
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public CheckableRelativeLayout(Context context, int checkableId) {
		super(context);
		init(null);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	// Checkable members
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		for (Checkable checkable : checkableViews) {
			checkable.setChecked(isChecked);
		}
	}

	public void toggle() {
		this.isChecked = !this.isChecked;
		for (Checkable checkable : checkableViews) {
			checkable.toggle();
		}
	}
	
	// Read the custom XML attributes
	private void init(AttributeSet attrs) {
		this.isChecked = false;
		this.checkableViews = new ArrayList<Checkable>(5);
	}
	
	// Add to our checkable list all the children of the view that implements 
	// the interface Checkable
	private void findCheckableChildren(View v) {
		if (v instanceof Checkable) {
			this.checkableViews.add((Checkable) v);
		}
		
		if (v instanceof ViewGroup) {
			final ViewGroup viewGroup = (ViewGroup) v;
			final int childCount = viewGroup.getChildCount();
			for (int i = 0; i < childCount; i++) {
				findCheckableChildren(viewGroup.getChildAt(i));
			}
		}
	}
}
