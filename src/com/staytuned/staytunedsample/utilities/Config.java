package com.staytuned.staytunedsample.utilities;

public class Config {

	public static final String LOCATION_KEY = "AIzaSyCWEIwEQhC0E7Ur0FPFY0kvHUFOQgYuROs";
	public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/search/json?radius=1000&sensor=true&key="
			+ LOCATION_KEY + "&location=";
	public static final long MIN_TIME = 5 * 1000 * 60;
	public static final float MIN_DIST = 1000;
	public static final String TABLE_MESSAGES = "MESSAGES";
	public static final String KEY_ID = "id";
	public static final String DATABASE_NAME = "STAYTUNED";
	public static final int DATABASE_VERSION = 1;
	public static final String KEY_MESSAGE = "msg";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TIME = "time";
	public static final String KEY_USERNAME = "username_1";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String PREF_NAME = "staytunedPrefs";
	public static final String PREF_USR_NAME = "user_name";
	public static final String PREF_USR_IMG = "user_image";
}
