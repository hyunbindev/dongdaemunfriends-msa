package com.baduk.baduk.service.blame;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.BlameExceptionConst;
import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.data.blame.BlameCommentDTO;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.blame.Blame;
import com.baduk.baduk.domain.blame.BlameComment;
import com.baduk.baduk.exception.BlameException;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.blame.BlameCommentRepository;
import com.baduk.baduk.repository.blame.BlameRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @version 1.1
 * @author hyunbinDev
 * target복수 적용
 * 
 * @since 2025-08-24
 * notification 기능 추가
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BlameCommentServiceImpl implements BlameCommentService{
	private final BlameCommentRepository blameCommentRepository;
	private final MemberRepository memberRepository;
	private final BlameMapperService blameMapperService;
	private final BlameRepository blameRepository;
	private final BlameNotificationService blameNotificationService;
	/**
	 * 
	 * @author hyunbinDev
	 * 저격글 덧글 생성
	 * 
	 * @author hyunbinDev
	 * @since 2025-08-24
	 * 덧글 작성시 notification event 발행
	 */
	@Override
	public void createBlameComment(BlameCommentDTO.Request.createBlameComment createDTO ,String authorUuid, Authentication authentication) {
		Member author = memberRepository.findById(authorUuid)
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		
		Blame blame = blameRepository.findById(createDTO.getBlameId())
				.orElseThrow(()-> new BlameException(BlameExceptionConst.NO_BLAME));
		
		/**
		 * @REFECT 복수 저격 대상 조건 문 수정
		 * comment 접근 권한 확인 로직 수정
		 */
		if(targetedCheck(blame.getTargetUuids(), authentication.getName()))
			throw new MemberException(MemberExceptionConst.NO_AUTHORITY);
		
		
		
		BlameComment comment = BlameComment.builder()
				.blameId(createDTO.getBlameId())
				.authorUuid(author.getUuid())
				.text(createDTO.getText())
				.build();
		
		comment = blameCommentRepository.save(comment);
		
		
		//notification event 발행
		blameNotificationService.sendBlameCommentNotification(blame, author);
	}

	@Override
	public void deleteBlameComment(String blameCommentId, String authorUuid) {
		//작성자 조회
		Member author = memberRepository.findById(authorUuid)
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		//코멘트 조회
		BlameComment comment = blameCommentRepository.findById(blameCommentId)
				.orElseThrow(()-> new BlameException(BlameExceptionConst.NO_BLAME_COMMENT));
		
		//권한확인
		if(!author.getUuid().equals(comment.getAuthorUuid())) {
			throw new MemberException(MemberExceptionConst.NO_AUTHORITY);
		}
		
		blameCommentRepository.delete(comment);
	}

	@Override
	public CommonPageDTO<BlameCommentDTO.Response.BlameComment> getBlameCommentPage(String blameId,int page, Authentication authentication) {
		Blame blame = blameRepository.findById(blameId)
				.orElseThrow(()-> new BlameException(BlameExceptionConst.NO_BLAME));
		
		//저격 대상 필터링
		if(!blame.getTargetUuids().isEmpty() && targetedCheck(blame.getTargetUuids(),authentication.getName()))
			throw new MemberException(MemberExceptionConst.NO_AUTHORITY);
		
		//페이징 처리
		Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC,"createdAt"));
		Page<BlameComment> blameCommentPage = blameCommentRepository.findByBlameId(blameId, pageable);
		
		return new CommonPageDTO<BlameCommentDTO.Response.BlameComment>("blameComments",blameCommentPage, blameComment -> blameMapperService.mapBlameCommentDTO(blameComment));
	}
	
	/**
	 * @author hyunbinDev
	 * @return 접근 권한 확인
	 * @since 2025-06-05 
	 */
	private boolean targetedCheck(List<String> targetUuids, String userUuid) {
		if(targetUuids.isEmpty()) return false;
		if(targetUuids.contains(userUuid)) return true;
		return false;
	}
}
