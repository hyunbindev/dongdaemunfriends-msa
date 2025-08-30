package com.baduk.baduk.service.persona;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.persona.PersonaDTO;

public interface PersonaService {
	
	public void createPersona(Authentication authentication, MultipartFile voice);
	
	public CommonPageDTO<PersonaDTO.Response.PersonaElementDTO> getPersonaPage(Authentication authentication, int page);
	
	public PersonaDTO.Response.PersonaElementDTO getPersona(Authentication authentication,String personaUuid);
	
	public void deletePersona(Authentication authentication, String presonaUuid);
}