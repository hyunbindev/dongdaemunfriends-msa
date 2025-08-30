package com.baduk.baduk.service.judgment;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.baduk.baduk.data.judgment.JudgmentCommentDTO;
import com.baduk.baduk.data.member.MemberDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.judgment.JudgmentComment;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.service.member.MemberMapperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgmentMapperService {
	private final MemberRepository memberRepository;
	private final MemberMapperService memberMapperService;
	/**
	 * 
	 * @param judgmentComment
	 * @return JudgmentCommentDTO.Response.JudgmentComment 
	 */
	public JudgmentCommentDTO.Response.JudgmentComment mapJudgmentCommentDTO(JudgmentComment judgmentComment){
		Optional<Member> optionalAuthor = memberRepository.findById(judgmentComment.getAuthorUuid());
		MemberDTO.Response.MemberSimple authorSimpl = memberMapperService.getMemberSimpl(optionalAuthor);
		
		return JudgmentCommentDTO.Response.JudgmentComment.builder()
														  .judgmentCommentId(judgmentComment.getId())
														  .author(authorSimpl)
														  .createdAt(judgmentComment.getCreatedAt())
														  .text(judgmentComment.getText())
														  .build();
	}
}
