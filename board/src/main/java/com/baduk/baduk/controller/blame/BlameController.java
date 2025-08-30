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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.data.blame.BlameDTO;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.service.blame.BlameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class BlameController {
	private final BlameService blameService;
	/**
	 * @param page
	 * @param authentication
	 * @return
	 * 저격글 페이지 조회
	 */
	@GetMapping("/blame")
	public ResponseEntity<CommonPageDTO<BlameDTO.Response.Blame>> getBlamePage(@RequestParam("page")int page,Authentication authentication) {
		CommonPageDTO<BlameDTO.Response.Blame> responseBody = blameService.getBlameList(page,authentication);
		return ResponseEntity.ok(responseBody);
	}
	/**
	 * @param blameId
	 * @param authentication
	 * @return
	 * 저격글 개별조회
	 */
	@GetMapping("blame/{blameId}")
	public ResponseEntity<BlameDTO.Response.Blame> getBlame(@PathVariable("blameId")String blameId, Authentication authentication){
		String userUuid = authentication.getName();
		return ResponseEntity.ok(blameService.getBlame(userUuid, blameId, authentication));
	}
	
	/**
	 * 
	 * @param createDTO
	 * @param authentication
	 * @return
	 * 저격글 생성
	 */
	@PostMapping("/blame")
	public ResponseEntity<BlameDTO.Response.CreateBlame> createBlame(
			@RequestBody
			BlameDTO.Request.CreateBlame createDTO,
			Authentication authentication) {
		createDTO.setAuthorUuid(authentication.getName());
		BlameDTO.Response.CreateBlame response = blameService.createBlame(createDTO);
		return ResponseEntity.ok(response);
	}
	/**
	 * 
	 * @param blameId
	 * @param authentication
	 * @return
	 * 저격글 삭제
	 */
	@DeleteMapping("/blame")
	public ResponseEntity<Void> deleteMapping(@RequestParam("blameId") String blameId, Authentication authentication){
		String userUuid = authentication.getName();
		blameService.deleteBlame(userUuid, blameId);
		return ResponseEntity.noContent().build();
	}
}
