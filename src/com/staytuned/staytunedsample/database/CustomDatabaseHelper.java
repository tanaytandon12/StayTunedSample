package com.staytuned.staytunedsample.database;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.staytuned.staytunedsample.model.Item;
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
		create = "CREATE TABLE " + Config.TABLE_NAMES + "("
				+ Config.KEY_USERNAME + " TEXT PRIMARY KEY, " + Config.KEY_URI
				+ " TEXT)";
		db.execSQL(create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_MESSAGES);
		db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_NAMES);
		// Create tables again
		onCreate(db);
	}

	public void insertUserName(String userName, String uri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Config.KEY_USERNAME, userName);
		values.put(Config.KEY_URI, uri);
		db.insert(Config.TABLE_NAMES, null, values);
	}

	public Cursor doesUserExist(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + Config.TABLE_NAMES + " WHERE "
				+ Config.KEY_USERNAME + " LIKE '" + name + "'";
		return db.rawQuery(sql, null);
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
		Log.d("TA5G", values.toString());
		db.insert(Config.TABLE_MESSAGES, null, values);
	}

	public Cursor getUser(String userName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + Config.TABLE_NAMES + " WHERE "
				+ Config.KEY_USERNAME + " LIKE '" + userName + "'";
		Log.d("TAG", sql);
		return db.rawQuery(sql, null);
	}

	public void updateUserTable(String name, String uri) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + Config.TABLE_NAMES + " SET " + Config.KEY_URI
				+ " = '" + uri + "' WHERE " + Config.KEY_USERNAME + " LIKE '"
				+ name + "'";
		db.execSQL(sql);
	}

	public ArrayList<Item> getAllRows(String userName, String criteria) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + Config.TABLE_MESSAGES + " WHERE "
				+ Config.KEY_USERNAME + " LIKE '" + userName + "' ORDER BY "
				+ criteria + " DESC";
		Log.d("TA6G", sql);
		ArrayList<Item> items = new ArrayList<>();
		Cursor c = db.rawQuery(sql, null);

		Log.d("TILL", c.getCount() + "");
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Item item = new Item();
			String mLatLng = c.getString(c.getColumnIndex(Config.KEY_LATITUDE))
					+ ", "
					+ c.getString(c.getColumnIndex(Config.KEY_LONGITUDE));
			long timeStamp = c.getLong(c.getColumnIndex(Config.KEY_TIMESTAMP));
			Date date = new Date(timeStamp);
			item.setLatLng(mLatLng);
			item.setTime(new SimpleDateFormat("yyyy-MM-dd").format(date));
			item.setContent(c.getString(c.getColumnIndex(Config.KEY_MESSAGE)));
			item.setTitle(c.getString(c.getColumnIndex(Config.KEY_TITLE)));
			Log.d("TILL", item.getContent() + item.getTitle());
			items.add(item);
		}

		return items;
	}
}
