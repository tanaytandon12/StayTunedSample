package com.staytuned.staytunedsample.utilities;

import java.util.Calendar;

public class CustomUtilities {

	private static Calendar instance;

	public static String getTime() {
		getCalendarInstance();
		String time = getHour() + " : " + getMinute();
		return time;
	}

	public static String getTimeBasedKey() {
		getCalendarInstance();
		String key = getDate() + " - " + getMonth() + " - " + getYear();
		return key;
	}

	private static Calendar getCalendarInstance() {
		if (instance == null) {
			instance = Calendar.getInstance();
		}
		return instance;
	}

	private static int getDate() {
		return instance.get(Calendar.DAY_OF_MONTH);
	}

	private static int getHour() {
		return instance.get(Calendar.HOUR_OF_DAY);
	}

	private static int getMonth() {
		return instance.get(Calendar.MONTH);
	}

	private static int getYear() {
		return instance.get(Calendar.YEAR);
	}

	private static int getMinute() {
		return instance.get(Calendar.MINUTE);
	}
}
