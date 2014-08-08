package com.hereshem.arduino;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ServerRequest {

	public String requestGetHttp(String url) {
		try {
			HttpGet request = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			return "{\"type\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
		}
	}

	public String requestGetHttp(String url, ArrayList<BasicNameValuePair> parameters) {
		return requestGetHttp(url + "?" 	+ URLEncodedUtils.format(parameters, "utf-8"));
	}

	/**
	 * post parameter contains ArrayList<BasicNameValuePair> parameters = new
	 * ArrayList<NameValuePair>(); parameters.add(new
	 * BasicNameValuePair("image",image_str));
	 **/

	public String requestPostHttp(String url, ArrayList<BasicNameValuePair> parameters) {
		try {
			HttpPost request = new HttpPost(url);
			HttpClient client = new DefaultHttpClient();
			if(parameters != null){
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
				request.setEntity(formEntity);
			}
			HttpResponse response = client.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			return "{\"type\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
		}
	}

	public static boolean isNetworkConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return false; // There are no active networks.
		} else
			return true;
	}

}