package com.letsplaydota.slowgram;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		token = getIntent().getStringExtra(getString(R.string.intent_key_auth_token));
		if (token == null) {
			throw new RuntimeException("no auth token in intent extra for MainActivity");
		}
		Toast.makeText(this, token, Toast.LENGTH_LONG);
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.main_layout_fragment);
		if (fragment == null) {
			fragment = new DialogListFragment();
			fm.beginTransaction().add(R.id.main_layout_fragment, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case R.id.action_settings:
				return true;

			case R.id.action_add:
				getFragmentManager().beginTransaction().replace(R.id.main_layout_fragment, new AddContactFragment())
						.addToBackStack(null).commit();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		getFragmentManager().popBackStack();
	}
}
