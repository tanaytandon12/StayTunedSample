package com.staytuned.staytunedsample.utilities;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomUtilities {

	private static Calendar instance;
	private static String userName;
	
	public static void setUserName(String name) {
		userName = name;
	}
	
	public static String getuserName() {
		return userName;
	}
	
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
	
	public static boolean validateName(String txt) {
		 
	    String regx = "^[\\p{L} .'-]+$";
	    Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(txt);
	    return matcher.find();
	 
	} 
}
