package com.baduk.baduk.service.news;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.constants.excpetion.NewsExceptionConst;
import com.baduk.baduk.data.news.RecommendStatusDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.domain.news.NewsRecommend;
import com.baduk.baduk.domain.news.RecommendType;
import com.baduk.baduk.exception.NewsException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.news.NewsRecommendRepository;
import com.baduk.baduk.repository.news.NewsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsRecommendService {
	private final MemberRepository memberRepository;
	private final NewsRepository newsRepository;
	private final NewsRecommendRepository newsRecommendRepository;
	
	@Transactional
	public RecommendStatusDTO recommendNews(Long newsId, RecommendType type, Authentication auth) {
		Member recommender = memberRepository.findById(auth.getName())
				.orElseThrow(()-> new NewsException(MemberExceptionConst.NO_AUTHORITY));
		
		News news = newsRepository.findById(newsId)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS));
		
		boolean isRecommended = newsRecommendRepository.existsByNewsAndRecommender(news,recommender);
		
		if(isRecommended) throw new NewsException(NewsExceptionConst.DUPLICATED_NEWS_RECOMMEND);
		
		NewsRecommend recommend = new NewsRecommend(news, recommender, type);
		
		newsRecommendRepository.save(recommend);
		
		return newsRecommendRepository.getRecommendStatus(news, recommender);
	}
	
	@Transactional
	public RecommendStatusDTO undoReCommend(Long newsId, Authentication auth) {
		Member recommender = memberRepository.findById(auth.getName())
				.orElseThrow(()-> new NewsException(MemberExceptionConst.NO_AUTHORITY));
		
		News news = newsRepository.findById(newsId)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS));
		
		NewsRecommend recommend = newsRecommendRepository.findByNewsAndRecommender(news,recommender)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS_RECOMMEND));
		
		newsRecommendRepository.delete(recommend);
		return newsRecommendRepository.getRecommendStatus(news, recommender);
	}
	
	public RecommendStatusDTO getRecommendStatus(Long newsId, Authentication auth){
		Member recommender = memberRepository.findById(auth.getName())
				.orElseThrow(()-> new NewsException(MemberExceptionConst.NO_AUTHORITY));
		
		News news = newsRepository.findById(newsId)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS));
		
		return newsRecommendRepository.getRecommendStatus(news, recommender);
	}
}
