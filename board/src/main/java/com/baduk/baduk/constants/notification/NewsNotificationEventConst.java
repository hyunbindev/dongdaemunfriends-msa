package com.baduk.baduk.constants.notification;


public enum NewsNotificationEventConst implements NotificationEventConst{
	NEWS_COMMENT_EVENT("news","comment");

	private final String SERVICE;
	private final String EVENT;
	
	
	NewsNotificationEventConst(String service, String event){
		this.SERVICE = service;
		this.EVENT = event;
	}
	
	
	@Override
	public String getService() {
		return this.SERVICE;
	}

	@Override
	public String getEvent() {
		return this.EVENT;
	}
}
