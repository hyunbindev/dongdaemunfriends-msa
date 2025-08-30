package com.baduk.baduk.service.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.constants.excpetion.NewsExceptionConst;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;
import com.baduk.baduk.data.news.NewsCommentRequestDTO;
import com.baduk.baduk.data.news.NewsCommentResponseDTO;
import com.baduk.baduk.data.news.NewsCommentResponseDTO.NewsComment.NewsCommentBuilder;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.news.News;
import com.baduk.baduk.domain.news.NewsComment;
import com.baduk.baduk.exception.NewsException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.news.NewsCommentRepository;
import com.baduk.baduk.repository.news.NewsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsCommentService {
	private final MemberRepository memberRepository;
	private final NewsRepository newsRepository;
	private final NewsCommentRepository newsCommentRepository;
	private final NewsNotificationService newsNotificationService;
	/**
	 * 덧글 생성
	 * @author hyunbinDev
	 * @param dto
	 * @param auth
	 * 
	 * @since 2025-08-24
	 * news comment Notification pub 추가
	 */
	@Transactional
	public void createNewsComment(NewsCommentRequestDTO.Create dto, Authentication auth) {
		String memberUuid = auth.getName();
		News news = newsRepository.findById(dto.getNewsId())
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS));
		
		Member member = memberRepository.findById(memberUuid)
				.orElseThrow(()-> new NewsException(MemberExceptionConst.NO_AUTHORITY));
		
		NewsComment comment = NewsComment.builder()
				.news(news)
				.author(member)
				.text(dto.getText())
				.build();
		
		newsCommentRepository.save(comment);
		
		//notification pub
		newsNotificationService.sendNewsCommentNotification(news, member);
	}
	/**
	 * 덧글 페이징
	 * @param newsId
	 * @param page
	 * @return
	 */
	public CommonPageDTO<NewsCommentResponseDTO.NewsComment> getNewsCommentPage(Long newsId, int page){
		News news = newsRepository.findById(newsId)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS));
		
		Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<NewsComment> newsCommentPage = newsCommentRepository.findByNews(news,pageable);
		
		return new CommonPageDTO<NewsCommentResponseDTO.NewsComment>("newsComments",newsCommentPage,(entity)-> mappingNewsComment(entity));
	}
	
	@Transactional
	public void deleteNewsComment(Long commentId, Authentication auth) {
		NewsComment entity = newsCommentRepository.findById(commentId)
				.orElseThrow(()-> new NewsException(NewsExceptionConst.NO_NEWS_COMMENT));
		String userUuid = auth.getName();
		
		if(!entity.getNews().getAuthor().getUuid().equals(userUuid)) throw new NewsException(MemberExceptionConst.NO_MEMBER);
		
		entity.softDelete();
	}
	/**
	 * 아 이것도 매핑.... 
	 * crud는 슬 귀찮네 토일 반납하고 코딩한지 3주째
	 * 삭제 분기 매핑 처리
	 * @param entity
	 * @return
	 */
	private NewsCommentResponseDTO.NewsComment mappingNewsComment(NewsComment entity){
		
		Member authorEntity = entity.getAuthor();
		String authorUuid = authorEntity.getUuid();
		String authorName = authorEntity.getName();
		String authorProfile = authorEntity.getProfile();
		
		MemberSimple author = new MemberSimple(authorUuid,authorName,authorProfile);
		
		NewsCommentBuilder NewsCommentDTOBuilder = NewsCommentResponseDTO.NewsComment.builder().id(entity.getId());
		
		//삭제 되서 덧글은 안보여줄거지만 프론트에서 삭제처리된 덧글이라고 알려줄거임 ㅇㅇ
		if(entity.isDeleted()) {
			 NewsCommentDTOBuilder.deleted(entity.isDeleted());
			 return NewsCommentDTOBuilder.build();
		}
		
		return NewsCommentDTOBuilder.author(author)
									.text(entity.getText())
									.createdAt(entity.getCreatedAt())
									.deleted(entity.isDeleted())
									.build();
	}
}
