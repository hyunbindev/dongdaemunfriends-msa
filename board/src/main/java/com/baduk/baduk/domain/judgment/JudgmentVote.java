package com.baduk.baduk.domain.judgment;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "judgmentVote")
@CompoundIndex(def = "{'judgmentId': 1, 'selectionId': 1 ,'voterUuid: 1'},unique = true")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JudgmentVote {
	@Id
	private String id;
	private String judgmentId;
	private String selectionId;
	private String voterUuid;
}
