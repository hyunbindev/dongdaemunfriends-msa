package com.baduk.baduk.data.judgment;

import java.util.List;

import com.baduk.baduk.data.common.CommonVoteSelectionResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hyunbinDev
 * 
 * vote DTO for judgment
 */

public class JudgmentVoteDTO {
	public class Request{
		@Getter
		@Setter
		public static class CreateVote{
			private String judgmentId;
			private String selectionId;
		}
	}
	
	public class Response{
		@Getter
		@Setter
		@AllArgsConstructor
		@NoArgsConstructor
		public static class VoteResult {
			private String judgmentId;
			private boolean isVoted;
			private List<CommonVoteSelectionResult> voteResult;
		}
	}
}
