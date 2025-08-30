package com.baduk.baduk.controller.judgment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.judgment.JudgmentCommentDTO;
import com.baduk.baduk.service.judgment.JudgmentCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/judgmentComment")
@RequiredArgsConstructor
@Slf4j
public class JudgmentCommentController {
	private final JudgmentCommentService judgmentCommentService;
	/**
	 * 
	 * @param judgmentId
	 * @param page
	 * @return
	 * 
	 * 재판 덧글 조회
	 */
	@GetMapping("/{judgmentId}")
	public ResponseEntity<CommonPageDTO<JudgmentCommentDTO.Response.JudgmentComment>>getJudgmentComment(
			@PathVariable
			("judgmentId")String judgmentId,
			@RequestParam("page")
			int page)
	{
		return ResponseEntity.ok(judgmentCommentService.getJudgmentCommentPage(judgmentId, page));
	}
	/**
	 * 
	 * @param createDTO
	 * @param judgmentId
	 * @param authentication
	 * @return
	 * 
	 * 재판 덧글 생성
	 */
	@PostMapping("/{judgmentId}")
	public ResponseEntity<Void>createJudgmentComment(
			@RequestBody 
			JudgmentCommentDTO.Request.CreateComment createDTO,
			@PathVariable("judgmentId")
			String judgmentId,
			Authentication authentication)
	{
		createDTO.setJudgmenttId(judgmentId);
		judgmentCommentService.createJudgmentComment(createDTO, authentication);
		return ResponseEntity.ok().build();
	}
	/**
	 * 
	 * @param commentId
	 * @param authentication
	 * @return
	 * 
	 * 재판 덧글 삭제
	 */
	@DeleteMapping("/{judgmentCommentId}")
	public ResponseEntity<Void> deleteJudgmentComment(
			@PathVariable("judgmentCommentId")
			String commentId,
			Authentication authentication){
		judgmentCommentService.deleteJudgmentComment(commentId, authentication);
		return ResponseEntity.noContent().build();
	}
}
