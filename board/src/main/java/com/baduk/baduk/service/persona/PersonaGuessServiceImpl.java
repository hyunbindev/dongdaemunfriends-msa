package com.baduk.baduk.service.persona;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baduk.baduk.constants.excpetion.PersonaExceptionConst;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;
import com.baduk.baduk.data.persona.PersonaDTO;
import com.baduk.baduk.data.persona.PersonaDTO.Response.PersonaGuessDTO;
import com.baduk.baduk.domain.persona.Persona;
import com.baduk.baduk.domain.persona.PersonaGuessAttempt;
import com.baduk.baduk.domain.persona.PersonaGuessAttempt.PersonaGuessAttemptBuilder;
import com.baduk.baduk.exception.PersonaException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.persona.PersonaGuessAttemptRepository;
import com.baduk.baduk.repository.persona.PersonaRepository;
import com.baduk.baduk.service.member.MemberMapperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonaGuessServiceImpl implements PersonaGuessService{
	private final PersonaGuessAttemptRepository personaGuessRepository;
	private final PersonaRepository personaRepository;
	private final MemberRepository memberRepository;
	private final MemberMapperService memberMapperService;
	
	@Override
	public PersonaGuessDTO guessPersona(Authentication authentication, PersonaDTO.Request.PersonaGuessDTO guessDto) {
		String userUuid = authentication.getName();
		String personaId = guessDto.getPersonaId();
		String guessAuthorUuid = guessDto.getGuessAuthorUuid();
		
		Optional<PersonaGuessAttempt> optionalGuessAttempt = personaGuessRepository.findByUserUuidAndTargetPersonaId(userUuid, personaId);
		//이미 작성자 추측 시도가 존재 할 경우
		if(optionalGuessAttempt.isPresent()) {
			throw new PersonaException(PersonaExceptionConst.DUPLICATED_PERSONA_ATTEMPT);
		}
		
		Persona persona = personaRepository.findById(personaId)
				.orElseThrow(()-> new PersonaException(PersonaExceptionConst.NO_Persona));
		
		//공통필드 빌더 생성
		PersonaGuessAttemptBuilder builder = PersonaGuessAttempt.builder()
																.userUuid(userUuid)
																.targetPersonaId(personaId)
																.submit(guessAuthorUuid);
		//응답 DTO
		PersonaGuessDTO responseDTO;
		
		//작성자를 맞췄다면 successDTO
		if(persona.getAuthorUuid().equals(guessAuthorUuid)) {
			builder.success(true);
			MemberSimple author = memberMapperService.getMemberSimpl(memberRepository.findById(persona.getAuthorUuid()));
			responseDTO = PersonaGuessDTO.getSuccessGuessDTO(author);
		}else {
			//틀렸다면 failDTO
			builder.success(false);
			responseDTO = PersonaGuessDTO.getFailGuessDTO();
		}
		//빌더 빌드
		PersonaGuessAttempt guessAttempt = builder.build();
		
		//attempt저장
		personaGuessRepository.save(guessAttempt);
		
		//response 리턴
		return responseDTO;
	}
}
