package com.baduk.baduk.domain.news;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.baduk.baduk.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 뉴스 추천 Entity
 * 캡슐화 원칙을 지향하기 위해 setter 사용 지양
 * @author 김현빈
 * @since 2025-07-18
 */
@Entity
@Getter
@NoArgsConstructor
public class NewsRecommend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//뉴스와 연관관계
	@ManyToOne
	private News news;
	
	//추천인
	@ManyToOne
	private Member recommender;
	
	//생성 날짜
	@CreatedDate
	private LocalDateTime createdAt;
	
	//추천 비추천 타입지정
	@Enumerated(EnumType.STRING)
	private RecommendType type;
	
	public NewsRecommend(News news ,Member recommender, RecommendType type) {
		this.news = news;
		this.recommender = recommender;
		this.type = type;
	}
}
