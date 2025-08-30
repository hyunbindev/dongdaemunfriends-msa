package com.baduk.baduk.domain.news;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * 뉴스 랭크 상위 집계 10개 뽑아내기위한 MYSQL VIEW TABLE ENTITY
 * @author hyunbinDev
 * @since 2025-07-20
 */
@Entity
@Immutable
@Table(name = "news_rank_view")
@Getter
public class NewsRankView {
	@Id
	@Column(name = "news_id")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "news_id", insertable = false, updatable = false)
	private News news;
	
	@Column(name = "rank_score")
	private Long score;
}
