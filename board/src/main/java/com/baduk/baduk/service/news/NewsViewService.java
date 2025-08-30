package com.baduk.baduk.service.news;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.repository.news.NewsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsViewService {
	private final RedisTemplate<String, String> newsViewRedisTemplate;
	private final NewsRepository newsRepository;
	private final NewsViewSyncService newsViewSyncService;
	private static final String REDIS_KEY_PATTERN = "news:viewed:";
	/**
	 * 사용자 조회 수 Redis 캐싱 처리 위해 Bean 주입
	 * 뷰카운트 00:00 시마다 배치처리
	 * @param newsViewRedisTemplate
	 */
	public NewsViewService(@Qualifier("newsViewRedisTemplate")
						   RedisTemplate<String, String> newsViewRedisTemplate,
						   NewsRepository newsRepository,
						   NewsViewSyncService newsViewSyncService)
	{
		this.newsViewRedisTemplate = newsViewRedisTemplate;
		this.newsRepository = newsRepository;
		this.newsViewSyncService = newsViewSyncService;
	}
	
	/**
	 * set 자료구조로 게시글당 userUuid로 뷰카운트 식별
	 * @param newsId
	 * @param userUuid
	 */
	public void recordViewCount(Long newsId, String userUuid) {
		String key = REDIS_KEY_PATTERN+newsId.toString();
		String value = userUuid;
		newsViewRedisTemplate.opsForSet().add(key, value);
	}
	/**
	 * 계시글 당 viewCount 조회
	 * @param newsId
	 * @return viewCount
	 */
	public Long getViewCount(Long newsId) {
		String key = REDIS_KEY_PATTERN+ newsId.toString();
		return newsViewRedisTemplate.opsForSet().size(key);
	}
	
	/**
	 * 00시마다 배치 처리 로직
	 * new키로 조회 하여 각 news Entity에 반영후 Redis캐시 정리
	 */
	//@Scheduled(cron = "*/10 * * * * *")
	@Scheduled(cron = "0 0 0 * * *")
	public void batchNewsViewCount() {
		/**
		 * 레디스 부하를 줄이기 위해 배치 반영시
		 * 1000개 단위로 키를 조회
		 * 서버기준 intel n100 cpu 코어 4개
		 * 이용자수는 적음 50개씩만 가져와서 병렬처리 해도 괜찮을거라 판단됨
		 * newsViewRedisTemplate.getConnectionFactory().getConnection().scan(options)<= 엥 이거 deprecated 처리 됐음
		 * 아마 현재 redis를 물고 있는 커넥션을 이용한 스캔이지 않을까?
		 * scan을 위해 새로 connection을 열고 다시 정리할 필요 없이 기존 연결된 connection을 사용해서 scan 할 수 있도록 api가 변경된거 같음
		 * 
		 * 시간나면 더 알아볼것!
		 * 아 새로운 커넥션을 만드는것이 아니라 이미 생성된 connection pool 에서 빌려오는 건데
		 * 직접 getConnection()은 너무 저수준에서 connectionpopl을 가져오느거라 개발자가 직접 관리할 요소가 많아 개발 위험 부담이 크다
		 * 즉 좀더 추상화 하여 위험요소를 제거하였기 때문에 deprecated 됨
		 * 
		 * Scanning 사용해서 좀더 안정성있게 가져옴
		 */
		log.info("######################DONGDAEMUN NEWS VIEWCOUNT BATCH START#######################");
		ScanOptions options = ScanOptions.scanOptions()
				.match(REDIS_KEY_PATTERN+"*")
				.count(50)
				.build();
		
		Cursor<String> cursor = newsViewRedisTemplate.scan(options);
		Map<Long,Long> viewCountMap = new HashMap<>();
		//try-with-resources 문은 기존 try-catch문과 다르게 finally로 자원할당 해재 해줄 필요가 없다!
		try(cursor){
			while(cursor.hasNext()) {
				String key = cursor.next();
				Long count = newsViewRedisTemplate.opsForSet().size(key);
				//news:viewed:`${viewCount}`이렇게 키값이 지정되어있을거기때문에 파싱 처리
				String[] parts = key.split(":");
				viewCountMap.put(Long.valueOf(parts[2]), count);
				//이후 값삭제
				newsViewRedisTemplate.delete(key);
				log.info("KEY : {} -- SUCCESS",key);
			}
		}catch(Exception e) {
			log.error("ViewCount 배치 처리 실패{}",e);
			//TODO-배치 처리 실패후 로직
			//1~5 중에 3에서 터졌을 시 
		}
		log.info("######################DONGDAEMUN NEWS VIEWCOUNT BATCH END#########################");
		//배치 병렬처리 순서 필요없기때문에
		viewCountMap.entrySet().parallelStream()
		.forEach((map)-> newsViewSyncService.syncNewsView(map.getKey(),map.getValue()));
	}
}













