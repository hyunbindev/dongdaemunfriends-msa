package com.baduk.baduk.service.persona;

import org.springframework.security.core.Authentication;

import com.baduk.baduk.data.persona.PersonaDTO;

public interface PersonaGuessService {
	public PersonaDTO.Response.PersonaGuessDTO guessPersona(Authentication authentication, PersonaDTO.Request.PersonaGuessDTO requestDTO);
}
