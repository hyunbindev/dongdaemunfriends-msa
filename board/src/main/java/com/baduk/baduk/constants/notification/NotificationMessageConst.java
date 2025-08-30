package com.baduk.baduk.constants.notification;

import lombok.Getter;

public enum NotificationMessageConst {
	NEWS_NOTIFICATION_MESSAGE("news-notification"),
	JUDGEMENT_NOTIFICATION_MESSAGE("judgement-notification"),
	BLAME_NOTIFICATION_MESSGAE("blame-notification"),
	PERSONA_NOTIFICATION_MESSAGE("persona-notification");
	
	@Getter
	private final String service;
	
	NotificationMessageConst(String service) {
		this.service = service;
	}
}