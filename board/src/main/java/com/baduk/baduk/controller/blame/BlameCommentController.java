package com.baduk.baduk.controller.blame;

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

import com.baduk.baduk.data.blame.BlameCommentDTO;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.service.blame.BlameCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class BlameCommentController {
	private final BlameCommentService blameCommentService;
	@GetMapping("/blameComment/{blameId}")
	public ResponseEntity<CommonPageDTO<BlameCommentDTO.Response.BlameComment>> getBlameComment(
			@PathVariable
			("blameId") String blameId,
			@RequestParam
			("page")int page,
			Authentication authentication)
	{
		return ResponseEntity.ok(blameCommentService.getBlameCommentPage(blameId, page,authentication));
	}
	
	@PostMapping("/blameComment/{blameId}")
	public ResponseEntity<Void> createBlameComment(
			@RequestBody 
			BlameCommentDTO.Request.createBlameComment createDTO, 
			@PathVariable("blameId")
			String blameId,
			Authentication authentication)
	{
		createDTO.setBlameId(blameId);
		blameCommentService.createBlameComment(createDTO, authentication.getName(),authentication);
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/blameComment/{blameCommentId}")
	public ResponseEntity<Void> deleteBlameComment(
			@PathVariable("blameCommentId")
			String blameCommentId,
			Authentication authentication){
		blameCommentService.deleteBlameComment(blameCommentId, authentication.getName());
		return ResponseEntity.noContent().build();
	}
}
