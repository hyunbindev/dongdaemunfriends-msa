package com.baduk.baduk.service.judgment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.notification.JudgmentNotificationEventConst;
import com.baduk.baduk.constants.rabbitmq.RabbitMQconstant;
import com.baduk.baduk.data.notification.NotificationEvent;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.judgment.Judgment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgmentNotificationService {
	private final RabbitTemplate rabbitTemplate;
	private final RabbitMQconstant config = RabbitMQconstant.NOTIFICATION;
	/**
	 * 재판글 덧글 작성되었을시 작성자에게 알림
	 * 비동기 처리 하여 응답 지연 최소화
	 * @since 2025-08-24
	 * @param judgment
	 * @param commentAuthor
	 */
	@Async
	public void sendJudgmentCommentNotification(Judgment judgment, Member commentAuthor) {
		//알림 받을 대상
		List<String> receivers = Arrays.asList(judgment.getAuthorUuid());
		
		//judgment Id
		String judgmentId = judgment.getId();
		
		//payLoad
		Map<String,String> payLoad = new HashMap<>();
		payLoad.put("commentAuthorName", commentAuthor.getName());
		payLoad.put("judgmentTitle", judgment.getTitle());
		
		
		//event 생성
		NotificationEvent event = NotificationEvent.builder()
				.receiver(receivers)
				.event(JudgmentNotificationEventConst.JUDGMENT_COMMENT_EVENT)
				.postId(judgmentId)
				.payLoad(payLoad)
				.build();
		
		rabbitTemplate.convertAndSend(config.EXCHANGE,config.ROUTING_KEY, event);
		
		log.info("judgment comment notification sent to {} for judgmentId {}", receivers, judgmentId);
	}
}