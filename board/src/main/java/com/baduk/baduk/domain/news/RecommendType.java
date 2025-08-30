package com.baduk.baduk.domain.news;

import lombok.Getter;

@Getter
public enum RecommendType {
	RECOMMEND("RECOMMEND"),
	UN_RECOMMEND("UN_RECOMMEND"),
	NO_RECOMMEND("NO_RECOMMEND");
	
	private final String type;
	
	RecommendType(String type) {
		this.type = type;
	}
}
