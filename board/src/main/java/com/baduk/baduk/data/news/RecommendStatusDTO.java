package com.baduk.baduk.data.news;

import com.baduk.baduk.domain.news.RecommendType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RecommendStatusDTO{
	private Long newsId;
	private Long recommendCount;
	private Long unRecommendCount;
	private RecommendType type;

	public RecommendStatusDTO(Long newsId, Long recommendCount, Long unRecommendCount, Integer status) {
		this.newsId = newsId;
		this.recommendCount = recommendCount;
		this.unRecommendCount = unRecommendCount;
		
		if(status == null) this.type = RecommendType.NO_RECOMMEND;
		
		switch(status) {
			case 1:
				this.type=RecommendType.RECOMMEND;
				break;
			case -1:
				this.type=RecommendType.UN_RECOMMEND;
				break;
			default:
				this.type=RecommendType.NO_RECOMMEND;
		}
	}
}