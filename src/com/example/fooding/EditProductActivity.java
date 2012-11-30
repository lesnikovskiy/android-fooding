package com.example.fooding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.example.fooding.R.id;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProductActivity extends Activity implements OnClickListener {
	private EditText productNameEditText;
	private EditText productPriceEditText;
	private TextView productIdTextView;
	private TextView productRevTextView;
	
	final static private String WEB_SERVICE_URL = "http://fooding.jit.su/api/products/update";
	
	//final static private String TAG = "MainFoodingActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_layout);
        
        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        String rev = intent.getExtras().getString("rev");
        String name = intent.getExtras().getString("name");
        String price = intent.getExtras().getString("price");
        
        View saveButton = this.findViewById(R.id.saveProductButton);
        saveButton.setOnClickListener(this);
        
        productIdTextView = (TextView) this.findViewById(R.id.id);
        productRevTextView = (TextView) this.findViewById(R.id.rev);
        productNameEditText = (EditText) this.findViewById(R.id.productName);
        productPriceEditText = (EditText) this.findViewById(R.id.productPrice);
        
        if (id != null)
        	productIdTextView.setText(id);
        
        if (rev != null)
        	productRevTextView.setText(rev);
        
        if (name != null)
        	productNameEditText.setText(name);
        
        if (price != null)
        	productPriceEditText.setText(price);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_fooding, menu);
        return true;
    }
    
    public void onClick(View v) {
    	switch(v.getId()) {
    		case R.id.saveProductButton:
    			String id = productIdTextView.getText().toString();
    			String name = productNameEditText.getText().toString();
    			String price = productPriceEditText.getText().toString();
    			String rev = productRevTextView.getText().toString();
    			
    			Toast.makeText(this, "You are about to post: "
    					+ id + " "
    					+ rev + " "
    					+ name + " " + price, Toast.LENGTH_LONG)
    				.show();
    			
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    			nameValuePairs.add(new BasicNameValuePair("id", id));
    			nameValuePairs.add(new BasicNameValuePair("rev", rev));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("price", price));   		   	
    			
				try {
					String response = HttpUtil.post(WEB_SERVICE_URL, nameValuePairs);
					
					Toast.makeText(this, response, Toast.LENGTH_LONG).show();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
				break;
    	}
    }

//    private String getFromUrl(String url) {
//    	String result = "";
//    	
//    	HttpClient client = new DefaultHttpClient();
//    	
//    	CookieStore cookieStore = new BasicCookieStore();
//		HttpContext context = new BasicHttpContext();
//		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
//		
//		HttpGet httpget = new HttpGet(url);
//		httpget.addHeader("Cookie", "foodingaccess=1-b8767f0228e22a1d3fe10e12e6d3d656");
//		
//		try {
//			HttpResponse httpResponse = client.execute(httpget);
//			
//			String line = "";
//			StringBuilder sb = new StringBuilder();
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(httpResponse.getEntity().getContent()));
//			
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);
//			}
//			
//			reader.close();
//			
//			result = sb.toString();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e(TAG, e.getMessage() + "\n" + e.getStackTrace());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e(TAG, e.getMessage() + "\n" + e.getStackTrace());
//		}
//
//		return result;
//    }
}
