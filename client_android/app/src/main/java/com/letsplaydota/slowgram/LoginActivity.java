package com.letsplaydota.slowgram;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {

	EditText phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		phoneNumber = (EditText) findViewById(R.id.phone_number);
		if (mPhoneNumber != null) {
			phoneNumber.setText(mPhoneNumber);
		}
		SharedPreferences preferences = getSharedPreferences(getString(R.string.auth_pref_name), MODE_PRIVATE);
		String token = preferences.getString(getString(R.string.pref_token), null);
		if (token != null) {
			Intent intent = new Intent().setClass(this, MainActivity.class);
			intent.putExtra(getString(R.string.intent_key_auth_token), token);
			finish();
			startActivity(intent);
		}
	}

	public void signIn(View v) {
		String mPhone = phoneNumber.getText().toString();
		new ServerPostAsyncTask(mPhone).execute();
	}

	public class ServerPostAsyncTask extends AsyncTask<Void, Void, Void> {

		String mPhone;

		public ServerPostAsyncTask(String phone) {
			this.mPhone = phone;
		}

		@Override
		protected Void doInBackground(Void... params) {
			String token = getToken();

			if (token != null) {
				SharedPreferences preferences = getSharedPreferences(getString(R.string.auth_pref_name), MODE_PRIVATE);
				preferences.edit().putString(getString(R.string.pref_token), token).commit();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						recreate();
					}
				});
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LoginActivity.this, "Error fetching token", Toast.LENGTH_LONG);
					}
				});
			}
			return null;
		}

		protected String getToken() {
			String url = "http://" +
					getString(R.string.server_ip) + ":" +
					getString(R.string.server_port) +
					getString(R.string.new_session_address);

			String phone_field = getString(R.string.new_session_phone_field);

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost sessionPostRequest = new HttpPost(url);
				List<NameValuePair> nameValuePairs = new ArrayList<>();
				nameValuePairs.add(new BasicNameValuePair(phone_field, mPhone));
				sessionPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpClient.execute(sessionPostRequest);

				String body = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(body);
				String token = jsonObject.getString(getString(R.string.new_session_token_field));
				return token;
			} catch (ClientProtocolException clientProtocolException) {
				clientProtocolException.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}

