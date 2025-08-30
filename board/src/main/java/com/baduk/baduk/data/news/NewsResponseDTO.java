package com.baduk.baduk.data.news;

import java.time.LocalDateTime;

import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class NewsResponseDTO {
	@Getter
	@Builder
	@AllArgsConstructor
	public static class News{
		private Long id;
		private String title;
		private MemberSimple author;
		private String imageUrl;
		private String text;
		private Long viewCount;
		private LocalDateTime createdAt;
	}
}