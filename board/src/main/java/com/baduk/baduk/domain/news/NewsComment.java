package com.baduk.baduk.domain.news;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.baduk.baduk.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


/**
 * 뉴스 덧글 Entity
 * 캡슐화 원칙을 지향하기 위해 setter 사용 지양
 * @author 김현빈
 * @since 2025-07-18
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@Getter
public class NewsComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//뉴스 연관관계 N+1 문제해결 위해 LAZY 타입 명시
	@ManyToOne(fetch = FetchType.LAZY)
	private News news;
	
	//작성자 N+1 문제해결 위해 LAZY 타입 명시
	@ManyToOne(fetch = FetchType.LAZY)
	private Member author;
	
	//본문 내용
	private String text;
	
	@ColumnDefault("false")
	@Builder.Default
	private boolean deleted=false;
	
	//생성날짜
	@CreatedDate
	private LocalDateTime createdAt;

	//jpa 프록시 객체 생성을 위한 기본 생성자 객체 생성시는 builder 패턴을 강제하여 실수 방지
	@SuppressWarnings("unused")
	private NewsComment() {}
	
	//soft delete할 예정
	public void softDelete() {
		this.deleted=true;
	}
}
