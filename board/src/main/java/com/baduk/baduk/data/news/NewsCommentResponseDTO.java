package com.baduk.baduk.data.news;

import java.time.LocalDateTime;

import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

import lombok.Builder;
import lombok.Getter;

public class NewsCommentResponseDTO {
	@Getter
	@Builder
	public static class NewsComment{
		private MemberSimple author;
		private Long id;
		private String text;
		private LocalDateTime createdAt;
		private boolean deleted;
	}
}






