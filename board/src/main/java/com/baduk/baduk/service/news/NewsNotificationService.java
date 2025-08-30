package com.baduk.baduk.service.news;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.notification.NewsNotificationEventConst;
import com.baduk.baduk.constants.rabbitmq.RabbitMQconstant;
import com.baduk.baduk.data.notification.NotificationEvent;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.news.News;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsNotificationService {
	private final RabbitTemplate rabbitTemplate;
	private final RabbitMQconstant config = RabbitMQconstant.NOTIFICATION;
	/**
	 * 뉴스에 덧글이 작성성되었을 시 작성자에게 알림
	 * 비동기 처리 하여 응답 지연 최소화
	 * @since 2025-08-24
	 * @param news
	 * @param author
	 */
	@Async
	public void sendNewsCommentNotification(News news, Member commentAuthor) {
		//알림 받을 대상
		List<String> receivers = Arrays.asList(news.getAuthor().getUuid());
		
		//news Id
		String newsId = news.getId().toString();
		
		//페이로드 생성
		Map<String,String> payLoad = new HashMap<>();
		payLoad.put("commentAuthorName", commentAuthor.getName());
		payLoad.put("newsTitle", news.getTitle());
		
		NotificationEvent event = NotificationEvent.builder()
				.receiver(receivers)
				.postId(newsId)
				.payLoad(payLoad)
				.event(NewsNotificationEventConst.NEWS_COMMENT_EVENT)
				.build();
		
		//이벤트 발행
		rabbitTemplate.convertAndSend(config.EXCHANGE, config.ROUTING_KEY, event);
		
		log.info("News comment notification sent to {} for newsId {}", receivers, newsId);
	}
}