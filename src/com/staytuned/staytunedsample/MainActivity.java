package com.staytuned.staytunedsample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.staytuned.staytunedsample.adapter.CustomAdapter;
import com.staytuned.staytunedsample.database.CustomDatabaseHelper;
import com.staytuned.staytunedsample.service.PeriodicService;
import com.staytuned.staytunedsample.utilities.Config;
import com.staytuned.staytunedsample.utilities.CustomUtilities;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		OnItemSelectedListener {

	private static final int ENABLE_PROVIDER = 10;
	private static final int PHOTO_PICKER_ID = 100;

	private ImageView setImageView, userImageView;
	private View loginView, itemView;
	private EditText usrNameEdtTxt;
	private TextView usrNameTextView;
	private Spinner mSpinner;
	private ListView mListView;

	private SharedPreferences msharePrefs;
	private SharedPreferences.Editor editor;

	private CustomDatabaseHelper helper;
	private HashMap<String, String> criteriaMap = new HashMap<>();
	private Cursor mCursor;
	private CustomAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		msharePrefs = getSharedPreferences(Config.PREF_NAME,
				Context.MODE_PRIVATE);
		editor = msharePrefs.edit();

		loginView = findViewById(R.id.login_box);
		itemView = findViewById(R.id.list_box);

		helper = new CustomDatabaseHelper(MainActivity.this);
		
		mListView = (ListView) findViewById(R.id.item_list);
		mAdapter = new CustomAdapter(MainActivity.this, mCursor);
		
		mListView.setAdapter(mAdapter);
		
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("Place");
		list.add("Time");

		criteriaMap.put("Place", Config.KEY_TITLE);
		criteriaMap.put("Time", Config.KEY_TIMESTAMP);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mSpinner.setAdapter(dataAdapter);
		mSpinner.setOnItemSelectedListener(this);
		
		findViewById(R.id.login_btn).setOnClickListener(this);
		findViewById(R.id.enable_bttn).setOnClickListener(this);

		usrNameEdtTxt = (EditText) findViewById(R.id.edt_name);
		usrNameTextView = (TextView) findViewById(R.id.usr_name);

		userImageView = (ImageView) findViewById(R.id.usr_img);
		setImageView = (ImageView) findViewById(R.id.img_user);
		userImageView.setOnClickListener(this);
		setImageView.setOnClickListener(this);

		doLocationBasedAction();
		doPrefBasedAction();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		doPrefBasedAction();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			editor.clear().commit();
			doPrefBasedAction();
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
		case R.id.login_btn:
			String name = usrNameEdtTxt.getText().toString();
			if (CustomUtilities.validateName(name)) {
				loginView.setVisibility(View.GONE);
				itemView.setVisibility(View.VISIBLE);
				usrNameTextView.setText(name);
				editor.putString(Config.PREF_USR_NAME, name).commit();
			} else {
				Toast.makeText(MainActivity.this, "Enter a valid name",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.img_user_txt:
		case R.id.usr_img:
		case R.id.img_user:
			Intent photoPickerIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			Log.d("TAG", "starting activity");
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
			Log.d("TAG", "wtf now");
			setImageView.setImageURI(uri);
			userImageView.setImageURI(uri);
			editor.putString(Config.PREF_USR_IMG, uri.toString()).commit();
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

	private void doPrefBasedAction() {
		String name = msharePrefs.getString(Config.PREF_USR_NAME, "Jane Doe");
		if (name == null) {
			loginView.setVisibility(View.VISIBLE);
			Uri imgUri = Uri.parse("android.resource://" + getPackageName()
					+ "/drawable/user");
			setImageView.setImageURI(imgUri);
			itemView.setVisibility(View.GONE);
		} else {
			loginView.setVisibility(View.GONE);
			String prefUri = msharePrefs.getString(Config.PREF_USR_IMG, null);
			if (prefUri != null) {
				userImageView.setImageURI(Uri.parse(prefUri));
			}
			itemView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		String criteria =  criteriaMap.get(parent.getItemAtPosition(position).toString());
		String userName = msharePrefs.getString(Config.PREF_USR_NAME, "Jane Doe");
		mCursor = helper.getAllRows(userName, criteria);
		mAdapter.notifyDataSetChanged();
		Log.d("TAG", "hitler " + mCursor.getCount());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
