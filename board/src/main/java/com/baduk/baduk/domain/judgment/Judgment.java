package com.baduk.baduk.domain.judgment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "judgment")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Judgment {
	@Id
	private String id;
	
	private String authorUuid;
	
	private String title;
	
	private String text;
	
	private List<JudgmentVoteSelection> selections;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private Judgment() {}
}