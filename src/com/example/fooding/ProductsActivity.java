package com.example.fooding;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ProductsActivity extends Activity  {
	private final static String WEB_SERVICE_URL = "http://fooding.jit.su/api/products/list/";
	private final static String TAG = "ProductsActivity";
	
	private ListView listView;
	private ProductArrayAdapter productsArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.products_layout);
		
		try {
			String json = HttpUtil.get(WEB_SERVICE_URL);
			
			if (TextUtils.isEmpty(json)) {
				Log.e(TAG, "json is null or empty");
				return;
			}
			
			Gson gson = new Gson();
			ProductSet products = gson.fromJson(json, ProductSet.class);	
			
			if (products != null && products.getProducts().size() > 0) {
				listView = (ListView) findViewById(R.id.products);
				productsArrayAdapter = new ProductArrayAdapter(this, R.layout.product_list_item, products.getProducts());
				listView.setAdapter(this.productsArrayAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
						View v = listView.getChildAt(position);
						TextView idText = (TextView) v.findViewById(R.id.id);
						TextView revText = (TextView) v.findViewById(R.id.rev);
						TextView nameText = (TextView) v.findViewById(R.id.name);
						TextView priceText = (TextView) v.findViewById(R.id.price);
						
						Intent intent = new Intent(v.getContext(), EditProductActivity.class);
						if (idText != null)
							intent.putExtra("id", idText.getText().toString());						
						if(revText != null)
							intent.putExtra("rev", revText.getText().toString());
						if(nameText != null)
							intent.putExtra("name", nameText.getText().toString());
						if(priceText != null)
							intent.putExtra("price", priceText.getText().toString());
						startActivity(intent);
					}
				});
			}
			
		} catch (ClientProtocolException e) {
			String errorMessage = e.getMessage() + "\n" + e.getStackTrace().toString();
			Log.e(TAG, errorMessage);
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();	
		} catch (IOException e) {
			String errorMessage = e.getMessage() + "\n" + e.getStackTrace().toString();
			Log.e(TAG, errorMessage);
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
		catch (Exception e) {
			String errorMessage = e.getMessage() + "\n" + e.getStackTrace().toString();
			Log.e(TAG, errorMessage);
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}
}
