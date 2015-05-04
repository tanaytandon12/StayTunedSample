package com.staytuned.staytunedsample.model;

public class Item {

	private String latlng;
	private String title;
	private String content;
	private String time;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLatLng(String latlng) {
		this.latlng = latlng;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getLatLng() {
		return this.latlng;
	}
}
