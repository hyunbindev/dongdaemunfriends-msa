package com.baduk.baduk.controller.judgment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.judgment.JudgmentVoteDTO;
import com.baduk.baduk.service.judgment.JudgmentVoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/judgment/vote")
@RequiredArgsConstructor
@Slf4j
public class JudgmentVoteController {
	private final JudgmentVoteService judgmentVoteService;
	
	@PostMapping
	public ResponseEntity<Void> voteJudgment(Authentication authentication , @RequestBody JudgmentVoteDTO.Request.CreateVote createVoteDTO) {
		judgmentVoteService.createJudgmentVote(authentication.getName(), createVoteDTO);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping
	public ResponseEntity<Void> cancelJudgmentVote(Authentication authentication, @RequestParam("judgmentId") String judgmentId) {
		judgmentVoteService.cancelJudgmentVote(authentication.getName(), judgmentId);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<JudgmentVoteDTO.Response.VoteResult> getVoteResult(Authentication authentication, @RequestParam("judgmentId")String judgmentId){
		return ResponseEntity.ok(judgmentVoteService.getVoteResult(authentication.getName(), judgmentId));
	}
}
