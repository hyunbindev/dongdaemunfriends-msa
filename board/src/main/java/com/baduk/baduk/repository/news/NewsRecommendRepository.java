package com.baduk.baduk.repository.news;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baduk.baduk.data.news.RecommendStatusDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.domain.news.NewsRecommend;

public interface NewsRecommendRepository extends JpaRepository<NewsRecommend, Long>{
	@Modifying
	void deleteByNews(News news);
	
	boolean existsByNewsAndRecommender(News news, Member member);
	
	Optional<NewsRecommend> findByNewsAndRecommender(News news, Member member);
	/**
	 * JPQL은 SUB QUERY 가 안됨 우회적으로 1 ,-1 값으로 가져옴
	 * COALESCE => ORACLE NVL 과 똑같은 기능
	 * 점점 쿼리가 복잡해지고 JPQL 너무 제약사항이 많다
	 * 이제 슬슬 QUERYDSL 배울 필요가 느껴지네
	 * @param news
	 * @param member
	 * @return
	 */
	@Query("""
		    SELECT new com.baduk.baduk.data.news.RecommendStatusDTO(
		        nr.news.id,
		        COALESCE(SUM(CASE WHEN nr.type = 'RECOMMEND' THEN 1 ELSE 0 END),0),
		        COALESCE(SUM(CASE WHEN nr.type = 'UN_RECOMMEND' THEN 1 ELSE 0 END),0),
		        COALESCE(MAX(
		            CASE WHEN nr.recommender = :recommender THEN
		                CASE 
		                    WHEN nr.type = 'RECOMMEND' THEN 1
		                    WHEN nr.type = 'UN_RECOMMEND' THEN -1
		                    ELSE 0
		                END
		            ELSE 0
		            END
		        ), 0)
		    )
		    FROM NewsRecommend nr
		    WHERE nr.news = :news
		""")
	RecommendStatusDTO getRecommendStatus(@Param("news")News news, @Param("recommender")Member member);
}
