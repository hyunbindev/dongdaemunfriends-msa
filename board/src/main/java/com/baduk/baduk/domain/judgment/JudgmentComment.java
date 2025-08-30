package com.baduk.baduk.domain.judgment;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.baduk.baduk.domain.common.CommonComment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection = "judgment_comment")
@Getter
@Setter
@SuperBuilder
public class JudgmentComment extends CommonComment{
	@Field("judgmentId")
	private String judgmentId;
	
	public JudgmentComment() {}
}
