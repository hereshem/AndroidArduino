package com.hereshem.arduino2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends Activity{
	SharedPreferences preferences;
	TextView tip, tpin;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.activity_setting);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		tip = (TextView) findViewById(R.id.txt_ip);
		tpin = (TextView) findViewById(R.id.txt_pin);
		
		tip.setText(preferences.getString("preIp", "http://192.168.11.177"));
		tpin.setText(preferences.getString("prePin", "1234"));
		
	}
	public void onClickFeature(View v){
		String ip = tip.getText().toString();
		String pin = tpin.getText().toString();
		preferences.edit().putString("preIp", ip).commit();
		preferences.edit().putString("prePin", pin).commit();
		finish();
	}
}
