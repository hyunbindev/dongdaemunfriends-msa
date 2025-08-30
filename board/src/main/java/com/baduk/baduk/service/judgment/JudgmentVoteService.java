package com.baduk.baduk.service.judgment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.JudgmentExceptionConst;
import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.data.common.CommonVoteSelectionResult;
import com.baduk.baduk.data.judgment.JudgmentVoteDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.judgment.Judgment;
import com.baduk.baduk.domain.judgment.JudgmentVote;
import com.baduk.baduk.exception.JudgmentException;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.judgment.JudgmentRepository;
import com.baduk.baduk.repository.judgment.JudgmentVoteRepository;
import com.baduk.baduk.template.JudgmentVoteTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
/**
 * @author hyunbinDev
 * 
 * judgmentvote Service
 */

public class JudgmentVoteService {
	private final JudgmentVoteRepository judgmentVoteRepository;
	private final JudgmentRepository judgmentRepository;
	private final MemberRepository memberRepository;
	private final JudgmentVoteTemplate judgmentVoteTemplate;
	/**
	 * @author hyunbinDev
	 * 
	 * judgment Vote method
	 */
	public void createJudgmentVote(String userUuid, JudgmentVoteDTO.Request.CreateVote voteCreateDTO) {
		//Judgment 게시글의 유효성 검사
		Judgment judgment = judgmentRepository.findById(voteCreateDTO.getJudgmentId())
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT, "삭제되었거나 잘못된 재판의 투표입니다."));
		
		
		//중복 투표 검사
		boolean existedVote = judgmentVoteRepository.existsByVoterUuidAndJudgmentId(userUuid, voteCreateDTO.getJudgmentId());
		//이미 투표 진행시 exception throw
		if(existedVote) throw new JudgmentException(JudgmentExceptionConst.DUPLICATED_VOTE);
		
		//새 투표 생성 로직
		JudgmentVote vote = JudgmentVote.builder()
				.judgmentId(voteCreateDTO.getJudgmentId())
				.selectionId(voteCreateDTO.getSelectionId())
				.voterUuid(userUuid)
				.build();
		
		judgmentVoteRepository.save(vote);
	}
	/**
	 * @author hyunbinDev
	 * @param userUuid
	 * @param judgmentId
	 * 
	 * @since 2025-05-02
	 * cancel judgmentVote method
	 */
	public void cancelJudgmentVote(String userUuid, String judgmentId) {
		JudgmentVote vote = judgmentVoteRepository.findByVoterUuidAndJudgmentId(userUuid,judgmentId)
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT_VOTE));
		judgmentVoteRepository.delete(vote);
	}
	/**
	 * @author hyunbinDev
	 * 
	 * @read
	 * get Vote result 
	 */
	public JudgmentVoteDTO.Response.VoteResult getVoteResult(String userUuid, String judgmentId){
		Member member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		
		Judgment judgment = judgmentRepository.findById(judgmentId)
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT));
		
		Optional<JudgmentVote> optionalVote = judgmentVoteRepository.findByVoterUuidAndJudgmentId(member.getUuid(), judgmentId);
		
		//투표 이력이 있을경우 로직 -> 집계후 로직
		if(optionalVote.isPresent()) {
			List<CommonVoteSelectionResult> resultList = judgmentVoteTemplate.getVoteCountFromJudgment(judgmentId);
			
			Map<String,CommonVoteSelectionResult> selectionResultMap = new HashMap<>();
			
			judgment.getSelections().stream()
				.forEach((selection)-> selectionResultMap.put(selection.getId(), new CommonVoteSelectionResult(selection.getId(),selection.getTitle(),0,false)));
			
			for(CommonVoteSelectionResult result : resultList) {
				CommonVoteSelectionResult temp = selectionResultMap.get(result.getSelectionId());
				temp.setVoteCount(result.getVoteCount());
			}
			
			CommonVoteSelectionResult votedResult = selectionResultMap.get(optionalVote.get().getSelectionId());
			votedResult.setVoted(true);
			
			List<CommonVoteSelectionResult> results = new ArrayList<>(selectionResultMap.values());
			
			return new JudgmentVoteDTO.Response.VoteResult(judgment.getId(),true, results);
		}
		//투표 결과가 없을 경우 로직
		List<CommonVoteSelectionResult> selectionResults = judgment.getSelections().stream()
				.map((selection) -> CommonVoteSelectionResult
						.builder()
						.selectionId(selection.getId())
						.selectionTitle(selection.getTitle())
						.build())
				
				.collect(Collectors.toList());
		return new JudgmentVoteDTO.Response.VoteResult(judgment.getId(),false,selectionResults);
	}
}
