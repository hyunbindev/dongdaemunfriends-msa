package com.baduk.baduk.domain.blame;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.baduk.baduk.domain.common.CommonComment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection = "blame_comment")
@Getter
@Setter
@SuperBuilder
public class BlameComment extends CommonComment{
	@Field("blameId")
	private String blameId;
	
	public BlameComment() {}
}