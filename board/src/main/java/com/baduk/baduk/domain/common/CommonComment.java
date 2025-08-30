package com.baduk.baduk.domain.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CommonComment {
	@Id
	private String id;
	
	@Field("authorUuid")
	private String authorUuid;
	
	@Field("text")
	private String text;
	
	@Field("createdAt")
	@CreatedDate
	private LocalDateTime createdAt;
	
	public CommonComment() {}
}
