package com.baduk.baduk.data.news;

import lombok.Getter;


public class NewsCommentRequestDTO {
	@Getter
	public static class Create{
		private Long newsId;
		private String text;
	}
}
