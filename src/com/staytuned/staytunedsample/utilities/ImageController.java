package com.staytuned.staytunedsample.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.staytuned.staytunedsample.listener.RequestListener;

public class ImageController extends AsyncTask<String, String, Bitmap> {

	private static RequestListener mRequestListener;

	@Override
	protected Bitmap doInBackground(String... params) {
		ImageLruCache cache = new ImageLruCache();
		Bitmap bitmap = cache.getBitMap(params[0]);
		
		if(bitmap != null) {
			return bitmap;
		}
		
		InputStream is = null;
		try {
			URL url = new URL(params[0]);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			httpUrlConnection.connect();
			is = httpUrlConnection.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			cache.putBitmap(params[0], bitmap);
			return bitmap;
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			mRequestListener.postRequest(result);
		}
	}

	public static void setRequestListener(RequestListener mListener) {
		mRequestListener = mListener;
	}
}
