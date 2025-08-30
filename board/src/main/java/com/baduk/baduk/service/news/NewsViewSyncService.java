package com.baduk.baduk.service.news;

import org.springframework.stereotype.Service;

import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.repository.news.NewsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
/**
 * 트랜잭션 어노테이션은 SpringAOP기반의 프록시 패턴 으로 동작한다.
 * 같은 클래스에서 트랜잭션 어노테이션이 붙어 있는 메서드를 호출한다고하면
 * this.method(); 로 실행 되기에 프록시 객체가 호출하는게 아닌
 * 단순 메서드가 실행된다
 * 
 * 트랜잭션 어노테이션이 붙어있는 메서드를 프록시 객체가 실행하기위해서는
 * 외부에서 호출 해야함으로 NewsViewSyncService를 따로 분리한다.
 * 
 * @param newsId
 * @param ViewCount
 */
@Service
@RequiredArgsConstructor
public class NewsViewSyncService {
	private final NewsRepository newsRepository;

	
	@Transactional
	public void syncNewsView(Long newsId, Long ViewCount) {
		News entity = newsRepository.findById(newsId)
				.orElse(null);
		if(entity == null) return;
		
		entity.syncViewCountIcrement(ViewCount);
	}
}
