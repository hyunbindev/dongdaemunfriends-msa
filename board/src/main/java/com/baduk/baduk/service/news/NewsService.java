package com.baduk.baduk.service.news;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.constants.excpetion.NewsExceptionConst;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;
import com.baduk.baduk.data.news.NewsRequestDTO;
import com.baduk.baduk.data.news.NewsResponseDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.domain.news.News.NewsBuilder;
import com.baduk.baduk.exception.NewsException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.news.NewsCommentRepository;
import com.baduk.baduk.repository.news.NewsRankViewRepository;
import com.baduk.baduk.repository.news.NewsRecommendRepository;
import com.baduk.baduk.repository.news.NewsRepository;
import com.baduk.baduk.utils.file.ImageConvertor;
import com.baduk.baduk.utils.file.MediaFormat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {
	private final MemberRepository memberRepository;
	private final NewsRepository newsRepository;
	private final NewsRecommendRepository newsRecommendRepository;
	private final NewsCommentRepository newsCommentRepository;
	private final NewsRankViewRepository newsRankViewRepository;
	
	private final NewsFileService newsFileService;
	private final NewsViewService newsViewService;
	
	
	private final MediaFormat MIME_WEBP =  MediaFormat.WEBP;
	
	@Value("${minio.public-url}")
	private String PUBLIC_URL;
	
	/**
	 * @author hyunbinDev
	 * @param dto
	 * @param iamge
	 * @return
	 */
	@Transactional
	public long createNews(NewsRequestDTO.Create dto, MultipartFile iamge) {
		Member member = memberRepository.findById(dto.getAuthorUuid())
				.orElseThrow(()-> new NewsException(MemberExceptionConst.NO_AUTHORITY));
		
		/**
		 * @REPECT
		 * @author hyunbinDev
		 * @since 2025-07-27
		 * 이미지가 없을 경우 Builder 객체로 분기
		 */
		NewsBuilder newsBuilder = News.builder()
				.title(dto.getTitle())
				.author(member)
				.text(dto.getText());
		
		if(iamge==null) {
			return newsRepository.save(newsBuilder.build()).getId();
		}
		//webp로전환
		//추후 파일은 추상화 할 예정
		
		byte[] webpByte = ImageConvertor.convert(iamge, MIME_WEBP);
		
		String imageObjectKey = newsFileService.fileUploadByByteArray(webpByte,MIME_WEBP);
		
		News news = newsBuilder.imageObjectKey(imageObjectKey).build();
		
		return newsRepository.save(news).getId();
	};
	/**
	 * @author hyunbinDev
	 * @param newsId
	 * @param auth
	 */
	@Transactional
	public void deleteNews(Long newsId, Authentication auth) {
		String memberUuid = auth.getName();
		
		Member member = memberRepository.findById(memberUuid)
				.orElseThrow(()-> new NewsException(MemberExceptionConst.NO_AUTHORITY));
		
		News targetNews = newsRepository.findById(newsId)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS));
		
		//권한검증
		if(!targetNews.getAuthor().getUuid().equals(member.getUuid())) throw new NewsException(MemberExceptionConst.NO_AUTHORITY);
		
		//연관 entity 삭제
		newsCommentRepository.deleteByNews(targetNews);
		newsRecommendRepository.deleteByNews(targetNews);
		newsRepository.delete(targetNews);
	}
	/**
	 * @author hyunbinDev
	 * @param newsId
	 * @return
	 */
	public NewsResponseDTO.News getNews(Long newsId, Authentication auth) {
		News news = newsRepository.findNewsWithAuthor(newsId)
				.orElseThrow(()->new NewsException(NewsExceptionConst.NO_NEWS));
		
		newsViewService.recordViewCount(newsId,auth.getName());
		return mapNewsDTO(news);
	}
	/**
	 * 단일 News 조회
	 * @author hyunbinDev
	 * @param page
	 * @return
	 */
	public CommonPageDTO<NewsResponseDTO.News> getNewsPage(int page) {
		Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<News> newsPage = newsRepository.findAll(pageable);
		
		return new CommonPageDTO<NewsResponseDTO.News>("news", newsPage, news -> mapNewsDTO(news));
	}
	/**
	 * 랭크 알고리즘 통하여
	 * View Table에서
	 * 상위 10개 가져옴
	 * @param page
	 * @return
	 */
	public List<NewsResponseDTO.News> getNewsByRank(){
		List<News> rankedNews = newsRankViewRepository.getNewsByRankView();
		return rankedNews.stream()
						 .map(entity -> mapNewsDTO(entity))
						 .collect(Collectors.toList());
	}
	
	/**
	 * 로직없는 단순 read 라서 MapperClass는 service 내부로직으로 처리
	 * @auth 김현빈
	 * @param news
	 * @return
	 */
	private NewsResponseDTO.News mapNewsDTO(News news){
		Member author = news.getAuthor();
		
		
		MemberSimple authorSimple = new MemberSimple(author.getUuid(),
													 author.getName(),
													 author.getProfile());
		
		Long todayViewCount = newsViewService.getViewCount(news.getId());
		
		NewsResponseDTO.News.NewsBuilder newsResponseBuilder = NewsResponseDTO.News.builder()
				.id(news.getId())
				.title(news.getTitle())
				.author(authorSimple)
				.text(news.getText())
				.viewCount(news.getViewCount() + todayViewCount)
				.createdAt(news.getCreatedAt());
		
		if(news.getImageObjectKey() == null ||news.getImageObjectKey().isEmpty()) {
			return newsResponseBuilder.build();
		}
		
		URL imageObjectPreSignedURL = newsFileService.getFileUrl(Arrays.asList(news.getImageObjectKey())).get(news.getImageObjectKey());
		String imageObjectURL = PUBLIC_URL + imageObjectPreSignedURL.getPath() + "?" + imageObjectPreSignedURL.getQuery();
		
		return newsResponseBuilder
				.imageUrl(imageObjectURL)
				.build();
	}
}