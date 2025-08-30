package com.baduk.baduk.data.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonVoteSelectionResult {
	private String selectionId;
	private String selectionTitle;
	private long voteCount;
	private boolean isVoted;
}
