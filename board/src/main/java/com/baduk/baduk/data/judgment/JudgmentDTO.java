package com.baduk.baduk.data.judgment;

import java.time.LocalDateTime;
import java.util.List;

import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author hyunbinDev
 * 동대문 제판 DTO
 * 
 * 
 */
public class JudgmentDTO {
	/**
	 * judgment Request DTO
	 * 
	 */
	public class Request{
		@Getter
		@Setter
		public static class CreateJudgment{
			private String title;
			private String text;
			private List<JudgmentSelection> selections;
		}
		
		/**
		 * judgment selectionDTO
		 */
		@Getter
		public static class JudgmentSelection{
			private String title;
		}
	}
	/**
	 * judgment Response DTO
	 */
	public class Response{
		@Getter
		@Setter
		@Builder
		public static class Judgment{
			private String id;
			private MemberSimple author;
			private String title;
			private String text;
			private LocalDateTime createdAt;
			private List<JudgmentVoteSelection> selections;
			private long commentCount;
			private int voteCount;
		}
		@Getter
		@AllArgsConstructor
		public static class JudgmentVoteSelection{
			private String id;
			private String title;
		}
	}
}
