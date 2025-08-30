package com.baduk.baduk.data.judgment;

import java.time.LocalDateTime;

import com.baduk.baduk.data.member.MemberDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hyunbindev
 * 
 * Judgment CommentDTO
 * 
 */

public class JudgmentCommentDTO {
	public class Request{
		@Getter
		@Setter
		public static class CreateComment{
			private String judgmenttId;
			private String text;	
		}
	}
	
	public class Response{
		@Getter
		@Setter
		@Builder
		public static class JudgmentComment{
			private String judgmentCommentId;
			private MemberDTO.Response.MemberSimple author;
			private LocalDateTime createdAt;
			private String text;	
		}
	}
}
