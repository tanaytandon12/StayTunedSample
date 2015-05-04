package com.staytuned.staytunedsample.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.staytuned.staytunedsample.MainActivity;
import com.staytuned.staytunedsample.R;
import com.staytuned.staytunedsample.database.CustomDatabaseHelper;
import com.staytuned.staytunedsample.listener.RequestListener;
import com.staytuned.staytunedsample.utilities.Config;
import com.staytuned.staytunedsample.utilities.CustomUtilities;
import com.staytuned.staytunedsample.utilities.ImageController;
import com.staytuned.staytunedsample.utilities.JSONController;
import com.staytuned.staytunedsample.utilities.NotificationID;

public class PeriodicService extends Service implements LocationListener,
		RequestListener, OnClickListener {

	private LocationManager mLocationManager;
	private double latitude = 28.0;
	private double longitude = 77.5;
	private CustomDatabaseHelper helper;
	private WindowManager wm;
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		JSONController.setRequestListener(this);

		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		helper = new CustomDatabaseHelper(PeriodicService.this);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location == null) {
			location = mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				Config.MIN_TIME, Config.MIN_DIST, this);
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, Config.MIN_TIME,
				Config.MIN_DIST, this);

		return Service.START_STICKY;
	}

	private void makeRequest() {
		String queryStr = latitude + "," + longitude;
		new JSONController().execute(queryStr);
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		makeRequest();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preRequest() {

	}

	@Override
	public void postRequest(Object object) {
		String result = object.toString();
		Log.d("TAG", result);

		try {
			JSONObject obj = new JSONObject(result);
			JSONArray results = obj.getJSONArray("results");
			if (results.length() != 0) {
				JSONObject json = results.getJSONObject(0);

				// The title of the notification
				final String name = json.getString("name");
				// The icon of the notification
				String icon = json.getString("icon");
				// create the message of the notification
				String content = "You are near " + name;
				JSONArray types = json.getJSONArray("types");
				if (types.length() >= 1) {
					content = content + ", it is a " + types.getString(0);
				}
				final String message = content + ", near "
						+ json.getString("vicinity") + ".";

				ImageController.setRequestListener(new RequestListener() {

					@Override
					public void preRequest() {

					}

					@Override
					public void postRequest(Object result) {
						Bitmap bitmap = (Bitmap) result;
						String key = name + CustomUtilities.getTimeBasedKey();
						String time = CustomUtilities.getTime();
						String userName = getSharedPreferences(
								Config.PREF_NAME, Context.MODE_PRIVATE)
								.getString(Config.PREF_USR_NAME, "Jane Doe");
						helper.insertRow(key, message, name, time, latitude,
								longitude, userName);
						PowerManager mPowerManager = (PowerManager) PeriodicService.this
								.getSystemService(Context.POWER_SERVICE);
						if (mPowerManager.isScreenOn()) {
							createWindow(bitmap, name, message, time);
						} else {
							createNotification(bitmap, name, message, time, key);
						}
					}
				});

				new ImageController().execute(icon);

			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	private void createWindow(Bitmap bitmap, String name, String message,
			String time) {
		final View view = LayoutInflater.from(PeriodicService.this).inflate(
				R.layout.notif_location, null);
		((TextView) view.findViewById(R.id.notif_title)).setText(name);
		((TextView) view.findViewById(R.id.notif_time)).setText(time);
		((TextView) view.findViewById(R.id.notif_content)).setText(message);
		((ImageView) view.findViewById(R.id.notif_icon)).setImageBitmap(bitmap);

		view.setBackgroundColor(getResources().getColor(android.R.color.black));

		// parameters of the window
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		view.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					// if the movement was not more than 20px in either
					// direction then the view is dismissed
					if ((params.x - initialX) < 20
							&& (params.y - initialY) < 20) {
						wm.removeView(view);
						// start the activity here
						Intent intent = new Intent(PeriodicService.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					return true;
				case MotionEvent.ACTION_MOVE:
					params.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					params.y = initialY
							+ (int) (event.getRawY() - initialTouchY);
					wm.updateViewLayout(view, params);
					return true;
				}
				return false;
			}
		});

		wm.addView(view, params);
	}

	private void createNotification(Bitmap bitmap, String name, String message,
			String time, String key) {
		NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(
				PeriodicService.this);
		RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
				R.layout.notif_location);

		Intent intent = new Intent(PeriodicService.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent mPendingIntent = PendingIntent.getActivity(
				PeriodicService.this, 0, intent, 0);

		mRemoteViews.setTextViewText(R.id.notif_title, name);
		mRemoteViews.setTextViewText(R.id.notif_content, message);
		mRemoteViews.setTextViewText(R.id.notif_time, time);
		mRemoteViews.setImageViewBitmap(R.id.notif_icon, bitmap);

		notifBuilder.setSmallIcon(R.drawable.ic_launcher);
		notifBuilder.setContent(mRemoteViews);
		notifBuilder.setContentIntent(mPendingIntent);

		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int id = NotificationID.getID(key);
		if (id != -1)
			mNotifyMgr.notify(NotificationID.getID(key), notifBuilder.build());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
