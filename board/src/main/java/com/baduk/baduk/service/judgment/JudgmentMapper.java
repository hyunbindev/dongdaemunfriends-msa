package com.baduk.baduk.service.judgment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baduk.baduk.data.judgment.JudgmentDTO;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.judgment.Judgment;
import com.baduk.baduk.domain.judgment.JudgmentVoteSelection;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.judgment.JudgmentCommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgmentMapper {
	private final MemberRepository memberRepository;
	private final JudgmentCommentRepository judgmentCommentRepository;
	/**
	 * 
	 * @param selectionsDTO
	 * @return List<JudgmentVoteSelection>
	 * @author hyunbinDev
	 * 
	 * judgment selection 을 object로 매핑
	 */
	public List<JudgmentVoteSelection> judgmentSelectionMapping(List<JudgmentDTO.Request.JudgmentSelection> selectionsDTO){
		return selectionsDTO.stream()
				.map(selection -> new JudgmentVoteSelection(selection.getTitle()))
				.collect(Collectors.toList());
	}
	/**
	 * @param judgment
	 * @return
	 * @author hyunbinDev
	 * 
	 * judgment document를 dto object로 매핑
	 */
	public JudgmentDTO.Response.Judgment judgmentMapping(Judgment judgment){

		Member member = memberRepository.findById(judgment.getAuthorUuid())
				.orElse(Member.builder().name("탈주한 사용자").build());                                               //사용자가 없을시
		

		MemberSimple authorSimpl = new MemberSimple(member.getUuid() ,member.getName() ,member.getProfile());       //간단 memberDTO 맵핑
		long commentCount = judgmentCommentRepository.countByJudgmentId(judgment.getId());
		return JudgmentDTO.Response.Judgment.builder()
				.id(judgment.getId())
				.author(authorSimpl)
				.title(judgment.getTitle())
				.text(judgment.getText())
				.createdAt(judgment.getCreatedAt())
				.commentCount(commentCount)
				.selections(judgment.getSelections().stream()                                                       //selection Object to DTO object mapping
						.map((selection) -> new JudgmentDTO.Response.JudgmentVoteSelection(selection.getId(), selection.getTitle()))
						.collect(Collectors.toList()))
				.build();
	}
}
