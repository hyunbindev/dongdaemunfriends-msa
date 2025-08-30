package com.baduk.baduk.data.blame;

import java.time.LocalDateTime;
import java.util.List;

import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BlameDTO {
	//RequestDTO
	public class Request{
		//Create New Blame request
		@Getter
		@Setter
		public static class CreateBlame{
			private String authorUuid;
			
			private String title;
			/**
			 * @REFECT 중복 타겟으로 전환
			 * String targetUuid;
			 */
			private List<String> targetUuids;
			
			private String content;
		}
	}
	public class Response{
		@Getter
		@Setter
		@Builder
		public static class Blame{
			private String id;
			
			private String title;
			
			private MemberSimple author;
			
			private boolean targeted;
			
			private List<MemberSimple> targets;
			
			private String text;
			
			private LocalDateTime createdAt;
			
			private long commentsCount;
		}
		@Getter
		@Setter
		@AllArgsConstructor
		public static class CreateBlame{
			private String blameId;
		}
	}
}