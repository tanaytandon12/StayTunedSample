package com.staytuned.staytunedsample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomAdapter extends BaseAdapter {

	private Cursor c;
	private Context ctxt;
	
	public CustomAdapter(Context context, Cursor cursor) {
		this.ctxt = context;
		this.c = cursor;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return c.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
