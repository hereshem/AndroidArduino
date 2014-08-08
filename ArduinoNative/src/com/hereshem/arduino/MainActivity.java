package com.hereshem.arduino;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	String domain = "http://192.168.0.177/";
	String pin = "1234";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		domain = preferences.getString("preIp", domain);
		pin = preferences.getString("prePin", pin);
		
		findViewById(R.id.btn_forward).setOnClickListener(this);
		findViewById(R.id.btn_reverse).setOnClickListener(this);
		findViewById(R.id.btn_right).setOnClickListener(this);
		findViewById(R.id.btn_left).setOnClickListener(this);
		findViewById(R.id.btn_stop).setOnClickListener(this);
	}
	
	public void requestServer(String url){
		if(ServerRequest.isNetworkConnected(getApplicationContext())){
			new Async(url).execute();
		}
		else{
			toast("No internet Connetcion");
		}
	}
	class Async extends AsyncTask<Void, Void, String>{

		String url;
		Async(String url){
			this.url = url;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			ServerRequest request = new ServerRequest();
			return request.requestGetHttp(url);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			TextView txt = (TextView) findViewById(R.id.txt_response);
			txt.setText("Url = " + url + "\n\nResponse = \n" + result);
		}
	}

	/*
	 * This function is triggered by setting onclick value for the view in layout 
	 * */
	@Override
	public void onClick(View v) {
		String action = "0";
		switch (v.getId()) {
			case R.id.btn_forward:
				action = "1";
				toast("Forward");
				break;
			case R.id.btn_reverse:
				action = "2";
				toast("Reverse");
				break;
			case R.id.btn_right:
				action = "3";
				toast("Right");
				break;
			case R.id.btn_left:
				action = "4";
				toast("Left");
				break;
			case R.id.btn_stop:
				action = "0";
				toast("Stop");
				break;
			default:
				break;
		}
		requestServer(domain + "?action=" + action);
	}
	
	public void toast(String string) {
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_setting, menu);
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.menu_settings){
    		startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    	}
    	else{
    		finish();
    		startActivity(getIntent());
    	}
    	return true;
    }
}
