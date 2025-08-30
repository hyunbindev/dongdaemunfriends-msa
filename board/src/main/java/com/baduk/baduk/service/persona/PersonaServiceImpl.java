package com.baduk.baduk.service.persona;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.constants.excpetion.PersonaExceptionConst;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.persona.PersonaDTO.Response.PersonaElementDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.persona.Persona;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.exception.PersonaException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.persona.PersonaRepository;
import com.baduk.baduk.utils.AnonNameUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hyunbinDev
 * @since 2025-06-18
 * 페르소나 service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonaServiceImpl implements PersonaService{
	private final PersonaMapperService personaMapperService;
	
	private final PersonaRepository personaRepository;
	
	@Qualifier("voiceMinioFileSerivce")
	private final PersonaFileService personaFileService;
	
	private final MemberRepository memberRepository;
	/**
	 * @author hyunbinDev
	 * @since 2025-06-18
	 * persona생성
	 */
	@Override
	public void createPersona(Authentication authentication, MultipartFile voice) {
		/**
		 * 저장예외 발생시 throw Error 하여 메소드를 중지시키기 위해 먼저 파일 저장
		 */
		//저장된 voice파일의 오브젝트 키
		String objectKey = personaFileService.fileUpload(voice);
		
		//Member 유효성 검사 및 memberEntity 조회
		Member author = memberRepository.findById(authentication.getName())
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		
		//Persona Document 인스턴트 생성
		Persona persona = Persona.builder()
								 .anonymousName(AnonNameUtil.getAnonName())
								 .authorUuid(author.getUuid())
								 .objectKey(objectKey)
								 .build();
		//저장
		personaRepository.save(persona);
	}
	/**
	 * @author hynbinDEv
	 * @since 2025-06-18
	 * 페르소나 페이지 조회
	 */
	@Override
	public CommonPageDTO<PersonaElementDTO> getPersonaPage(Authentication authentication, int page) {
		Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<Persona> personaPage = personaRepository.findAll(pageable);
		
		return new CommonPageDTO<PersonaElementDTO>("personas", personaPage, persona -> personaMapperService.mapPersonaDTO(persona, authentication.getName()));
	}
	/**
	 * @author hyunbinDEv
	 * @since 2025-06-18
	 * 페르소나 조회
	 * MapperServic로 분리 해야하지 않을까 너무 비대한데
	 */
	@Override
	public PersonaElementDTO getPersona(Authentication authentication, String personaUuid) {
		Persona persona = personaRepository.findById(personaUuid)
				.orElseThrow(()-> new PersonaException(PersonaExceptionConst.NO_Persona));
		return personaMapperService.mapPersonaDTO(persona, authentication.getName());
	}

	@Override
	public void deletePersona(Authentication authentication, String presonaUuid) {
		// TODO Auto-generated method stub
		
	}
}
