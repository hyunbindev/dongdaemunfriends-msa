package com.baduk.baduk.controller.judgment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.judgment.JudgmentDTO;
import com.baduk.baduk.service.judgment.JudgmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/judgment")
@RequiredArgsConstructor
@Slf4j
public class JudgmentController {
	private final JudgmentService judgmentService;
	/**
	 * @param authentication
	 * @param createDTO
	 * @return
	 * @since 2025-05-02
	 * @author hyunbinDev
	 * Judgment create
	 */
	@PostMapping
	public ResponseEntity<Void> createJudgment(Authentication authentication ,@RequestBody JudgmentDTO.Request.CreateJudgment createDTO){
		judgmentService.createJudgment(authentication.getName(), createDTO);
		return ResponseEntity.ok().build();
	}
	/**
	 * @param judgmentId
	 * @return
	 * @since 2025-05-02
	 * @author hyunbinDev
	 * 
	 * judgment get
	 */
	@GetMapping("/{judgmentId}")
	public ResponseEntity<JudgmentDTO.Response.Judgment> getJudgment(@PathVariable("judgmentId")String judgmentId) {
		return ResponseEntity.ok(judgmentService.getJudgment(judgmentId));
	}
	/**
	 * 
	 * @param page
	 * @return
	 * 
	 * judgmentPage 조회
	 */
	@GetMapping
	public ResponseEntity<CommonPageDTO<JudgmentDTO.Response.Judgment>> getJudgmentPage(@RequestParam("page")int page) {
		return ResponseEntity.ok(judgmentService.getJudgmentPage(page));
	}
}
