package com.staytuned.staytunedsample.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.staytuned.staytunedsample.R;
import com.staytuned.staytunedsample.model.Item;

public class CustomAdapter extends BaseAdapter {

	private ArrayList<Item> items;
	private Context ctxt;

	public CustomAdapter(Context context, ArrayList<Item> items) {
		this.ctxt = context;
		this.items = items;
	}

	public void print() {
		Log.d("TAG", "print");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {

		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Log.d("WILL", "SMOT");
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) ctxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_location, parent,
					false);
			holder = new ViewHolder();
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.item_content);
			holder.tvLatLng = (TextView) convertView
					.findViewById(R.id.item_lat_lng);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_time);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item item = (Item) getItem(position);

		holder.tvTitle.setText(item.getTitle());
		holder.tvContent.setText(item.getContent());
		holder.tvTime.setText(item.getTime());
		holder.tvLatLng.setText(item.getLatLng());

		return convertView;
	}

	private class ViewHolder {
		TextView tvTitle, tvContent, tvTime, tvLatLng;
	}

}
