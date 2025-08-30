package com.baduk.baduk.service.judgment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.JudgmentExceptionConst;
import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.judgment.JudgmentCommentDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.judgment.Judgment;
import com.baduk.baduk.domain.judgment.JudgmentComment;
import com.baduk.baduk.exception.BlameException;
import com.baduk.baduk.exception.JudgmentException;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.judgment.JudgmentCommentRepository;
import com.baduk.baduk.repository.judgment.JudgmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudgmentCommentService {
	private final JudgmentCommentRepository judgmentCommentRepository;
	private final MemberRepository memberRepository;
	private final JudgmentRepository judgmentRepository;
	private final JudgmentMapperService judgmentCommentMapperService;
	private final JudgmentNotificationService judgmentNotificationService;
	/**
	 * @author hyunbinDev
	 * @param createDTO
	 * @param authentication
	 * 
	 * Judgment comment 생성
	 */
	public void createJudgmentComment(JudgmentCommentDTO.Request.CreateComment createDTO, Authentication authentication) {
		Member author = memberRepository.findById(authentication.getName())
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		
		Judgment judgment = judgmentRepository.findById(createDTO.getJudgmenttId())
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT));
		
		//재판 덧글 객체 생성
		JudgmentComment comment = JudgmentComment.builder()
				.judgmentId(judgment.getId())
				.authorUuid(author.getUuid())
				.text(createDTO.getText())
				.build();
		
		//재판 덧글 저장
		judgmentCommentRepository.save(comment);
		
		//재판 알람
		judgmentNotificationService.sendJudgmentCommentNotification(judgment, author);
	}
	
	/**
	 * 
	 * @param judgmentCommentId
	 * @param authentication
	 * 
	 * judgment Comment 삭제
	 */
	public void deleteJudgmentComment(String judgmentCommentId, Authentication authentication) {
		Member author = memberRepository.findById(authentication.getName())
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		
		JudgmentComment comment = judgmentCommentRepository.findById(judgmentCommentId)
				.orElseThrow(()-> new BlameException(JudgmentExceptionConst.NO_JUDGMENT_COMMENT));
		
		if(!author.getUuid().equals(comment.getAuthorUuid())) {
			throw new MemberException(MemberExceptionConst.NO_AUTHORITY);
		}
		
		judgmentCommentRepository.delete(comment);
	}
	
	/**
	 * @author hyunbinDev
	 * @param judgmentId
	 * @param page
	 * @param authentication
	 * @return CommonPageDTO<JudgmentCommentDTO.Response.JudgmentComment>
	 * 
	 * Get judgmentCommentPage
	 */
	public CommonPageDTO<JudgmentCommentDTO.Response.JudgmentComment> getJudgmentCommentPage(String judgmentId,int page){
		Judgment judgment = judgmentRepository.findById(judgmentId)
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT_COMMENT));

		Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC,"createdAt"));
		
		Page<JudgmentComment> judgmentCommentPage = judgmentCommentRepository.findByJudgmentId(judgment.getId(), pageable);
		
		return new CommonPageDTO<JudgmentCommentDTO.Response.JudgmentComment>("judgmentComments", judgmentCommentPage, judgmentComment -> judgmentCommentMapperService.mapJudgmentCommentDTO(judgmentComment));
	}
}
