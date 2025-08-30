package com.baduk.baduk.repository.news;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baduk.baduk.domain.news.News;
public interface NewsRepository extends JpaRepository<News,Long>{
	@Query("""
			SELECT n FROM News n
			JOIN FETCH n.author
			WHERE n.id = :newsId
			""")
	Optional<News> findNewsWithAuthor(@Param("newsId") Long newsId);
	
	@EntityGraph(attributePaths = "author")
	Page<News> findAll(Pageable pageable);
}