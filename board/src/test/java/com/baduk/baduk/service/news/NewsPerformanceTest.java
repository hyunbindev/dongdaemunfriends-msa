package com.baduk.baduk.service.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;

import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.news.NewsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@Rollback
@Slf4j
public class NewsPerformanceTest {
	@Autowired
	NewsService newsService;
	
	@Autowired
	@Qualifier("newsViewRedisTemplate")
	private RedisTemplate<String, String> newsViewRedisTemplate;
	
	@Autowired
	private NewsRepository newsRepository;
	
	@Autowired
	private NewsViewService newsViewService;
	
	@Autowired
	private MemberRepository memberRepository;

	final int count = 1000;
	
	
	@Test
	void testViewCountwithRedisCaching() {
		for(int i = 0; i<count; i++) {
			log.info("test index {}",i);
			News news = newsRepository.findNewsWithAuthor(6L)
					.orElse(null);
			newsViewService.recordViewCount(news.getId(),"4213731531");
		}
		newsViewRedisTemplate.getConnectionFactory().getConnection().flushAll();
	}
	
	@Test
	@Transactional
	void testViewCountWithOutRedisCaching() {
		for(int i = 0; i<count; i++) {
			log.info("test index {}",i);
			News news = newsRepository.findNewsWithAuthor(6L)
					.orElse(null);
			news.syncViewCountIcrement(1L);
		}
	}
}
