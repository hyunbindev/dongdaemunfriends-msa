package com.baduk.baduk.controller.persona;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.persona.PersonaDTO.Response.PersonaElementDTO;
import com.baduk.baduk.service.persona.PersonaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/persona")
@RequiredArgsConstructor
@Slf4j
public class PresonaController {
	private final PersonaService personaService;
	/**
	 * 
	 * @param authentication
	 * @param voice
	 * @return
	 * 페르소나 생성
	 */
	@PostMapping
	public ResponseEntity<Void> createPersona(Authentication authentication, @RequestPart("voice-file") MultipartFile voice){
		personaService.createPersona(authentication, voice);
		return ResponseEntity.ok().build();
	}
	/**
	 * 
	 * @param authentication
	 * @param personaId
	 * @return
	 * 페르소나 개별 조회
	 */
	@GetMapping("/{personaId}")
	public ResponseEntity<PersonaElementDTO> getPersona(Authentication authentication, @PathVariable("personaId")String personaId){
		return ResponseEntity.ok(personaService.getPersona(authentication, personaId));
	}
	/**
	 * 
	 * @param authentication
	 * @param page
	 * @return
	 * 페르소나 페이지 조회
	 */
	@GetMapping
	public ResponseEntity<CommonPageDTO<PersonaElementDTO>> getPersonaPage(Authentication authentication, @RequestParam("page")int page){
		return ResponseEntity.ok(personaService.getPersonaPage(authentication, page));
	}
}
