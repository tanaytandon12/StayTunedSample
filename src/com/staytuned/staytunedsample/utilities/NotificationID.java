package com.staytuned.staytunedsample.utilities;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationID {
	private final static AtomicInteger c = new AtomicInteger(10);
	private static HashMap<String, Integer> idMap = new HashMap<>();
	
	public static int getID(String key) {
		int id = c.incrementAndGet();
		if(idMap.containsKey(key)) {
			return -1;
		}
		idMap.put(key, id);
		return id;
	}
	
	public static int getIdFromMap(String key) {
		return idMap.get(key);
	}
}
