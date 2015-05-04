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
				+ Config.KEY_TITLE + " TEXT," + Config.KEY_TIME + " TEXT)";
		Log.d("TAG", create);
		db.execSQL(create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_MESSAGES);
		// Create tables again
		onCreate(db);
	}

	public void insertRow(String id, String message, String title, String time) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Config.KEY_ID, id);
		values.put(Config.KEY_MESSAGE, message);
		values.put(Config.KEY_TITLE, title);
		values.put(Config.KEY_TIME, time);
		values.put(Config.KEY_TIMESTAMP, System.currentTimeMillis());
		// Inserting Row
		db.insert(Config.TABLE_MESSAGES, null, values);
	}

	public Cursor getAllRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + Config.TABLE_MESSAGES + " ORDER BY " + Config.KEY_TIMESTAMP + " DESC";
		return db.rawQuery(sql, null);
	}
}
