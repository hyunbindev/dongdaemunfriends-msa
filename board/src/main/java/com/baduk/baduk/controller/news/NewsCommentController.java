package com.baduk.baduk.controller.news;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.news.NewsCommentRequestDTO;
import com.baduk.baduk.data.news.NewsCommentResponseDTO;
import com.baduk.baduk.service.news.NewsCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class NewsCommentController {
	private final NewsCommentService newsCommentService;
	
	@PostMapping("/newscomment")
	public ResponseEntity<Void> createNewsComment(@RequestBody NewsCommentRequestDTO.Create dto, Authentication auth){
		newsCommentService.createNewsComment(dto, auth);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/{newsId}/newscomment")
	public ResponseEntity<CommonPageDTO<NewsCommentResponseDTO.NewsComment>> getNewsCommentPage(@PathVariable("newsId")Long newsId, @RequestParam(name="page", defaultValue = "0")int page){
		
		return ResponseEntity.ok(newsCommentService.getNewsCommentPage(newsId, page));
	}
	
	@DeleteMapping("/newscomment/{commentId}")
	public ResponseEntity<Void> delete(@PathVariable("commentId")Long commentId, Authentication auth){
		newsCommentService.deleteNewsComment(commentId, auth);
		return ResponseEntity.ok().build();
	}
}
