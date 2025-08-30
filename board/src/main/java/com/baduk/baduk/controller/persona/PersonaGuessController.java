package com.baduk.baduk.controller.persona;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.persona.PersonaDTO;
import com.baduk.baduk.service.persona.PersonaGuessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/persona/guess")
@RequiredArgsConstructor
@Slf4j
public class PersonaGuessController {
	private final PersonaGuessService personaGuessService;
	
	@PostMapping
	public ResponseEntity<PersonaDTO.Response.PersonaGuessDTO> guessPersonaAuthor(Authentication auth,
																				  @RequestBody
																				  PersonaDTO.Request.PersonaGuessDTO req)
	{
		return ResponseEntity.ok(personaGuessService.guessPersona(auth, req));
	}
}
