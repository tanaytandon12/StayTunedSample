package com.staytuned.staytunedsample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.staytuned.staytunedsample.utilities.Config;

public class CustomDatabaseHelper extends SQLiteOpenHelper {

	public CustomDatabaseHelper(Context context) {
		super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create = "CREATE TABLE " + Config.TABLE_MESSAGES + "("
				+ Config.KEY_TIMESTAMP + " INTEGER PRIMARY KEY, "
				+ Config.KEY_ID + " TEXT ," + Config.KEY_MESSAGE + " TEXT,"
				+ Config.KEY_TITLE + " TEXT," + Config.KEY_TIME + " TEXT,"
				+ Config.KEY_USERNAME + " TEXT," + Config.KEY_LATITUDE
				+ " REAL, " + Config.KEY_LONGITUDE + " REAL)";
		Log.d("TAG", create);
		db.execSQL(create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_MESSAGES);
		// Create tables again
		onCreate(db);
	}

	public void insertRow(String id, String message, String title, String time,
			double latitude, double longitude, String userName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Config.KEY_ID, id);
		values.put(Config.KEY_MESSAGE, message);
		values.put(Config.KEY_TITLE, title);
		values.put(Config.KEY_TIME, time);
		values.put(Config.KEY_TIMESTAMP, System.currentTimeMillis());
		values.put(Config.KEY_LATITUDE, latitude);
		values.put(Config.KEY_LONGITUDE, longitude);
		values.put(Config.KEY_USERNAME, userName);
		// Inserting Row
		Log.d("TAG", values.toString());
		db.insert(Config.TABLE_MESSAGES, null, values);
	}

	public Cursor getAllRows(String userName, String criteria) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + Config.TABLE_MESSAGES + " WHERE "
				+ Config.KEY_USERNAME + " LIKE '" + userName + "' ORDER BY "
				+ criteria + " DESC";
		Log.d("TAG", sql);
		return db.rawQuery(sql, null);
	}
}
