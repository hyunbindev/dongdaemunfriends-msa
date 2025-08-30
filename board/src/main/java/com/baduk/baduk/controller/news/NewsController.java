package com.baduk.baduk.controller.news;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.news.NewsRequestDTO;
import com.baduk.baduk.data.news.NewsResponseDTO;
import com.baduk.baduk.service.news.NewsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {
	private final NewsService newsService;
	
	@PostMapping
	public ResponseEntity<Void> createNews(
			Authentication auth,
			@RequestPart("dto")
			NewsRequestDTO.Create dto
			,@RequestPart(value="image",required = false)
			MultipartFile image) {
		dto.setAuthorUuid(auth.getName());
		newsService.createNews(dto, image);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{newsId}")
	public ResponseEntity<Void> deleteNews(@PathVariable("newsId")Long newsId, Authentication auth){
		newsService.deleteNews(newsId, auth);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{newsId}")
	public ResponseEntity<NewsResponseDTO.News> getNews(@PathVariable("newsId")Long newsId, Authentication auth){
		return ResponseEntity.ok(newsService.getNews(newsId,auth));
	}
	
	@GetMapping
	public ResponseEntity<CommonPageDTO<NewsResponseDTO.News>> getNewsPage(@RequestParam(name="page",defaultValue = "0")int page){
		return ResponseEntity.ok(newsService.getNewsPage(page));
	}
	
	@GetMapping("/rank")
	public ResponseEntity<List<NewsResponseDTO.News>> getNewsRank(){
		return ResponseEntity.ok(newsService.getNewsByRank());
	}
}