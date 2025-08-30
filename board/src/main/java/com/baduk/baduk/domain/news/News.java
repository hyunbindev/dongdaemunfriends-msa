package com.baduk.baduk.domain.news;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.baduk.baduk.domain.Member;

import jakarta.persistence.Column;
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
 * 뉴스 Entity
 * 캡슐화 원칙을 지향하기 위해 setter 사용 지양
 * UX 관점에서 사용자 오해와 혼동을 최소화 하기위해 삭제는 가능하지만 수정기능은 없을 예정
 * @author 김현빈
 * @since 2025-07-18
 */
@Entity
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//제목
	private String title;
	
	//작성자 N+1 문제해결 위해 LAZY 타입 명시
	@ManyToOne(fetch = FetchType.LAZY)
	private Member author;
	
	//이미지파일 오브젝트 키
	private String imageObjectKey;
	
	//본문 내용
	@Column(name = "text", columnDefinition = "TEXT")
	private String text;
	
	//생성날짜
	@CreatedDate
	private LocalDateTime createdAt;
	
	//조회수 자정마다 카운트 Redis로 기록후 00시 마다 배치처리
	@Builder.Default
	private int viewCount = 0;
	
	//jpa 프록시 객체 생성을 위한 기본 생성자 객체 생성시는 builder 패턴을 강제하여 실수 방지
	@SuppressWarnings("unused")
	private News() {}
	
	public void syncViewCountIcrement(Long viewCount) {
		this.viewCount+=viewCount;
	}
}
