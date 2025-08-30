package com.baduk.baduk.constants.notification;


/**
 * @author hyunbinDev
 * @since 2025-08-24
 * BlamenotificationEvent 타입
 */
public enum BlameNotificationEventConst implements NotificationEventConst{
	BLAME_TARGET_EVENT("blame","target"),
	BLAME_COMMENT_EVENT("blame","comment");
	
	private final String SERVICE;
	private final String EVENT;
	
	BlameNotificationEventConst(String service, String event){
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
