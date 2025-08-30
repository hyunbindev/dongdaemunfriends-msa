package com.baduk.baduk.service.judgment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.JudgmentExceptionConst;
import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.judgment.JudgmentDTO;
import com.baduk.baduk.domain.judgment.Judgment;
import com.baduk.baduk.domain.judgment.JudgmentVoteSelection;
import com.baduk.baduk.exception.JudgmentException;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.judgment.JudgmentRepository;
import com.baduk.baduk.repository.judgment.JudgmentVoteRepository;
import com.baduk.baduk.template.JudgmentVoteTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author hyunbinDev
 * 
 * JudgmentService
 */
@Service("JudgmentService")
@RequiredArgsConstructor
@Slf4j
public class JudgmentService {
	private final JudgmentRepository judgmentRepository;
	private final MemberRepository memberRepository;
	
	private final JudgmentVoteTemplate judgmentVoteTemplate;
	
	private final JudgmentMapper judgmentMapper;
	
	/**
	 * @author hyunbinDev
	 * @param authorUuid 
	 * @param createDTO
	 * 
	 * 재판 저장
	 */
	public void createJudgment(String authorUuid, JudgmentDTO.Request.CreateJudgment createDTO) {
		memberRepository.findById(authorUuid)
				.orElseThrow(()->new MemberException(MemberExceptionConst.NO_MEMBER));
		
		Judgment judgment = Judgment.builder()
				.authorUuid(authorUuid)
				.title(createDTO.getTitle())
				.text(createDTO.getText())
				.selections(judgmentMapper.judgmentSelectionMapping(createDTO.getSelections()))
				.build();
		
		judgmentRepository.save(judgment);
	}
	/**
	 * @author hyunbinDev
	 * @param judgmentId
	 * @return JudgmentDTO.Response.Judgment
	 * 
	 * 재판 본문 조회
	 */
	public JudgmentDTO.Response.Judgment getJudgment(String judgmentId){
		Judgment judgment = judgmentRepository.findById(judgmentId)
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT));
		
		return judgmentMapper.judgmentMapping(judgment);
	}
	/**
	 * 재판 선택지 조회
	 * 
	 * @author hyunbinDev
	 * 
	 */
	public List<JudgmentDTO.Response.JudgmentVoteSelection> getJudgmentSelection(String judgmentId){
		Judgment judgment = judgmentRepository.findById(judgmentId)
				.orElseThrow(()-> new JudgmentException(JudgmentExceptionConst.NO_JUDGMENT));
		List<JudgmentVoteSelection> selectionMap = judgment.getSelections();
		
		return selectionMap.stream()
				.map(selection -> new JudgmentDTO.Response.JudgmentVoteSelection(selection.getId(), selection.getTitle()))
				.collect(Collectors.toList());
	}
	/**
	 * @author hyunbinDev
	 * @param page
	 * @param isPreview -> true일경우 덧글과 투표수 조회 하여 리턴
	 * @return
	 * 
	 */
	public  CommonPageDTO<JudgmentDTO.Response.Judgment> getJudgmentPage(int page){
		Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Judgment> judgmentPage = judgmentRepository.findAll(pageable);
		//덧글과 투표수 조회 및 dto 리턴
		List<String> judgmentIdList = judgmentPage.getContent().stream()
				.map((judgment)-> judgment.getId())
				.collect(Collectors.toList());
		//TODO - 덧글 조회 아직 미구현 -> 코멘트 카운트와 함께 가져와야함
		Map<String,Integer> voteCountMap= judgmentVoteTemplate.getTotalVoteCountFromJudgment(judgmentIdList);
		
		CommonPageDTO<JudgmentDTO.Response.Judgment> pageDTO = new CommonPageDTO<JudgmentDTO.Response.Judgment>("judgments", judgmentPage, judgment->judgmentMapper.judgmentMapping(judgment));
		
		for(JudgmentDTO.Response.Judgment judgmentDTO : pageDTO.getContent().get("judgments")) {
			int voteCount = voteCountMap.getOrDefault(judgmentDTO.getId(), 0);
			judgmentDTO.setVoteCount(voteCount);
		}
		return pageDTO;
	}
}
