package com.fooding.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fooding.activities.R;
import com.fooding.models.Product;

public class ProductArrayAdapter extends ArrayAdapter<Product> {
	private final List<Product> products;	
	
	public ProductArrayAdapter(Context context, int resource, List<Product> products) {
		super(context, resource, products);
		this.products = products; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.product_list_item, null);
		}
		
		Product product = products.get(position);
		if (product != null) {
			TextView idTextView = (TextView) view.findViewById(R.id.product_id);
			TextView nameTextView = (TextView) view.findViewById(R.id.name);
			TextView priceTextView = (TextView) view.findViewById(R.id.price);
			
			if (idTextView != null) {
				idTextView.setText(String.valueOf(product.getProductId()));
			}
						
			if (nameTextView != null)
				nameTextView.setText(product.getName());
			
			if (priceTextView != null)
				priceTextView.setText(String.valueOf(product.getPrice()));
		}
		
		return view;
	}
}
