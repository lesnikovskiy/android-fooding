package com.fooding.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fooding.activities.R;
import com.fooding.entities.Product;

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
			TextView idText = (TextView) view.findViewById(R.id.id);
			TextView revText = (TextView) view.findViewById(R.id.rev);
			TextView nameText = (TextView) view.findViewById(R.id.name);
			TextView priceText = (TextView) view.findViewById(R.id.price);
			
			if (idText != null) {
				idText.setText(product.getId());
			}
			
			if (revText != null) {
				revText.setText(product.getRev());
			}
			
			if (nameText != null)
				nameText.setText(product.getName());
			
			if (priceText != null)
				priceText.setText(product.getPrice());
		}
		
		return view;
	}
}
