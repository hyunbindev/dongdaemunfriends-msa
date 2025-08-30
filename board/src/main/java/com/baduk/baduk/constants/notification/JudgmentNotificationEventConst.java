package com.baduk.baduk.constants.notification;

public enum JudgmentNotificationEventConst implements NotificationEventConst{
	JUDGMENT_COMMENT_EVENT("judgement","comment");

	private final String SERVICE;
	private final String EVENT;
	
	JudgmentNotificationEventConst(String service, String event){
		this.SERVICE = service;
		this.EVENT = event;
	}
	
	@Override
	public String getService() {
		return this.SERVICE;
	}

	@Override
	public String getEvent() {
		// TODO Auto-generated method stub
		return this.EVENT;
	}

}