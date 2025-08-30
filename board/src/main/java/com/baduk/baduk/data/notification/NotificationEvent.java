package com.baduk.baduk.data.notification;

import java.util.List;
import java.util.Map;

import com.baduk.baduk.constants.notification.NotificationEventConst;

import lombok.Getter;


public class NotificationEvent {
	//Notification 받을 대상
	@Getter
	private final List<String> receivers;
	//해당 책임 서비스
	@Getter
	private final String service;
	//해당 서비스에서 이밴트
	@Getter
	private final String event;
	//해당 이벤트 발생 게시글
	@Getter
	private final String postId;
	//추가 payLoad
	@Getter
	private final Map<String,String> payLoad;
	
	public static NotificationEventBuilder builder() {
		return new NotificationEventBuilder();
	}
	
	private NotificationEvent(NotificationEventBuilder builder) {
		this.receivers = builder.receivers;
		this.service = builder.event.getService();
		this.event = builder.event.getEvent();
		this.postId = builder.postId;
		this.payLoad = builder.payLoad;
	}
	
	public static class NotificationEventBuilder{
		private List<String> receivers;
		private NotificationEventConst event;
		private String postId;
		private Map<String,String> payLoad;
		
		public NotificationEventBuilder receiver(List<String> receivers) {
			this.receivers = receivers;
			return this;
		}
		
		public NotificationEventBuilder event(NotificationEventConst event) {
			this.event = event;
			return this;
		}
		
		public NotificationEventBuilder postId(String postId) {
			this.postId = postId;
			return this;
		}
		
		public NotificationEventBuilder payLoad(Map<String,String> payLoad) {
			this.payLoad = payLoad;
			return this;
		}
		
		public NotificationEvent build() {
			return new NotificationEvent(this);
		}
	}
}