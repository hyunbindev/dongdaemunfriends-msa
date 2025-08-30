package com.baduk.baduk.service.blame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.notification.BlameNotificationEventConst;
import com.baduk.baduk.constants.rabbitmq.RabbitMQconstant;
import com.baduk.baduk.data.notification.NotificationEvent;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.blame.Blame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlameNotificationService {
	private final RabbitTemplate rabbitTemplate;
	private final RabbitMQconstant config = RabbitMQconstant.NOTIFICATION;
	/**
	 * 저격 대상에게 알림
	 * 비동기 처리 하여 응답 지연 최소화
	 * @param blame
	 * @param target
	 * @since 2025-08-24
	 */
	@Async
	public void sendBlameTargetNotification(Blame blame) {
		//저격 대상(알림받을 대상)
		List<String> receivers = blame.getTargetUuids();
		//알림 받을 대상이 없을시 이벤트 발행 하지 않음
		if(receivers.size() == 0) return;
		//blame post id
		String blameId = blame.getId();
		
		//Notification Event 객체
		NotificationEvent event = NotificationEvent.builder()
				.receiver(receivers)
				.event(BlameNotificationEventConst.BLAME_TARGET_EVENT)
				.postId(blameId)
				.build();
		
		rabbitTemplate.convertAndSend(
				config.EXCHANGE, config.ROUTING_KEY, event);
		
		log.info("Blame comment notification sent to {} for commentId {}", receivers, blameId);
	}
	/**
	 * 저격글에 덧글이 들어왔을 시 작성자에게 알림
	 * 비동기 처리 하여 응답 지연 최소화
	 * @since 2025-08-24
	 * @param blame
	 * @param author
	 */
	@Async
	public void sendBlameCommentNotification(Blame blame, Member commentAuthor) {
		List<String> receivers = Arrays.asList(blame.getAuthorUuid());
		
		String blameId = blame.getId();
		
		//pay load 작성자및 덧글 내용
		Map<String,String> payLoad = new HashMap<>();
		payLoad.put("commentAuthorName", commentAuthor.getName());
		//Notification Event 객체
		NotificationEvent event = NotificationEvent.builder()
				.receiver(receivers)
				.event(BlameNotificationEventConst.BLAME_COMMENT_EVENT)
				.postId(blameId)
				.payLoad(payLoad)
				.build();
		
		rabbitTemplate.convertAndSend(config.EXCHANGE, config.ROUTING_KEY, event);
		
		log.info("Blame comment notification sent to {} for commentId {}", receivers, blameId);
	}
}