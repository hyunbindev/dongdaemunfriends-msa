package com.baduk.baduk.controller.news;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.news.RecommendStatusDTO;
import com.baduk.baduk.domain.news.RecommendType;
import com.baduk.baduk.service.news.NewsRecommendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class NewsRecommendController {
	private final NewsRecommendService newsRecommendService;
	
	@GetMapping("/news/{newsId}/recommend")
	public ResponseEntity<RecommendStatusDTO> getNewsRecommendStatus(@PathVariable("newsId")Long newsId, Authentication auth){
		return ResponseEntity.ok(newsRecommendService.getRecommendStatus(newsId, auth));
	}
	
	@PostMapping("/news/{newsId}/recommend")
	public ResponseEntity<RecommendStatusDTO> recommendNews(@PathVariable("newsId")Long newsId, @RequestParam("type")RecommendType type, Authentication auth){
		return ResponseEntity.ok(newsRecommendService.recommendNews(newsId, type, auth));
	}

	@DeleteMapping("/news/{newsId}/recommend")
	public ResponseEntity<RecommendStatusDTO> undoReCommend(@PathVariable("newsId")Long newsId, Authentication auth){
		return ResponseEntity.ok(newsRecommendService.undoReCommend(newsId, auth));
	}
}
