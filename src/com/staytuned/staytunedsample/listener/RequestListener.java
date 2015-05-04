package com.staytuned.staytunedsample.listener;

public interface RequestListener {

	public void preRequest();

	public void postRequest(Object result);
}