package com.baduk.baduk.repository.news;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.domain.news.NewsComment;

public interface NewsCommentRepository extends JpaRepository<NewsComment,Long> {
	@Modifying
	void deleteByNews(News news);
	
	@Query("SELECT c FROM NewsComment c JOIN FETCH c.author WHERE c.news = :news")
	Page<NewsComment> findByNews(@Param("news")News news, Pageable pageable);
	
	
	@Query("SELECT c FROM NewsComment c JOIN FETCH c.author WHERE c.id = :commentId")
	Optional<NewsComment> findById(@Param("commentId")Long commentId);
}