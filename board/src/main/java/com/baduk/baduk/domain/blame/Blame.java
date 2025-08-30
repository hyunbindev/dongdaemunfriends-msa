package com.baduk.baduk.domain.blame;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.1
 * target uuid 복수로 전환 업데이트
 * 
 */
@Document(collection = "blame")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Blame {
	@Id
	private String id;
	
	private String authorUuid;
	
	private String title;
	private String content;
	/**
	 * @REFECT 타겟 복수로 전환
	 * @since 2025-06-06
	 * private String targetUuid;
	 */
	private List<String> targetUuids;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@SuppressWarnings("unused")
	private Blame() {};
}
