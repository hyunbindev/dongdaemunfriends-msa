package com.baduk.baduk.data.blame;


import java.time.LocalDateTime;

import com.baduk.baduk.data.member.MemberDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BlameCommentDTO {
	public class Request{
		@Getter
		@Setter
		public static class createBlameComment{
			private String blameId;
			private String text;
		}
	}
	
	public class Response{
		@Getter
		@Setter
		@Builder
		public static class BlameComment{
			private String blameCommentId;
			
			private MemberDTO.Response.MemberSimple author;
			
			private LocalDateTime createdAt;
			
			private String text;
		}
	}
}
