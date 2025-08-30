package com.baduk.baduk.repository.news;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.domain.news.NewsRankView;

public interface NewsRankViewRepository extends JpaRepository<NewsRankView,Long>{

	@Query("""
			SELECT n
			FROM NewsRankView v
			JOIN FETCH News n ON v.id = n.id
			ORDER BY v.score DESC
			""")
	List<News> getNewsByRankView();
}
