package com.staytuned.staytunedsample.utilities;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageLruCache {

	private final static LruCache<String, Bitmap> cache = new LruCache<>(20);
	
	public Bitmap getBitMap(String url) {
		return cache.get(url);
	}
	
	public void putBitmap(String url, Bitmap map) {
		cache.put(url, map);
	}
	
}
