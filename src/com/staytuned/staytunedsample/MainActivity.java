package com.staytuned.staytunedsample;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.staytuned.staytunedsample.service.PeriodicService;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private static final int ENABLE_PROVIDER = 10;
	private static final int PHOTO_PICKER_ID = 100;

	private ImageView setImageView, userImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		userImageView = (ImageView) findViewById(R.id.usr_img);
		setImageView = (ImageView) findViewById(R.id.img_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.enable_bttn:
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, ENABLE_PROVIDER);

			break;
		case R.id.img_user_txt:
		case R.id.img_user:
			Intent photoPickerIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(photoPickerIntent, PHOTO_PICKER_ID);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENABLE_PROVIDER) {
			if (resultCode == RESULT_OK) {
				doLocationBasedAction();
			}
		} else if (requestCode == PHOTO_PICKER_ID && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			setImageView.setImageURI(uri);
			userImageView.setImageURI(uri);
		}
	}

	private void doLocationBasedAction() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Toast.makeText(MainActivity.this,
					"The service will start only when you enable location",
					Toast.LENGTH_LONG).show();
			(findViewById(R.id.enable_bttn)).setVisibility(View.VISIBLE);
		} else {
			Intent intent = new Intent(MainActivity.this, PeriodicService.class);
			startService(intent);
		}

	}
}
