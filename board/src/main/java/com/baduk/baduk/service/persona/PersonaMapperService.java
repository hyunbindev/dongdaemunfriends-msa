package com.baduk.baduk.service.persona;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baduk.baduk.data.member.MemberDTO;
import com.baduk.baduk.data.persona.PersonaDTO;
import com.baduk.baduk.data.persona.PersonaDTO.Response.PersonaElementDTO;
import com.baduk.baduk.domain.persona.Persona;
import com.baduk.baduk.domain.persona.PersonaGuessAttempt;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.persona.PersonaGuessAttemptRepository;
import com.baduk.baduk.service.member.MemberMapperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonaMapperService {
	private final PersonaGuessAttemptRepository personaGuessAttemptRepository;
	private final PersonaFileService personaFileService;
	private final MemberRepository memberRepository;
	private final MemberMapperService memberMapperService;
	@Value("${minio.public-url}")
	private String PUBLIC_URL;
	public PersonaElementDTO mapPersonaDTO(Persona persona,String userUuid) {
		
		//PersonaGuessAttempt 조회
		Optional<PersonaGuessAttempt> optionalGuessAttempt = personaGuessAttemptRepository.findByUserUuidAndTargetPersonaId(userUuid,persona.getId());
		
		URL objectPreSignedURL = personaFileService.getFileUrl(Arrays.asList(persona.getObjectKey())).get(persona.getObjectKey());
		
		/*
		 * GuessAttempt가 없을경우
		 * 이용자는 Persona작성자의 실체를 알 수 없으나, 추측은 가능한 상태
		 * reveal => false
		 * author => annoName
		 * 로 조회 하는 상태
		 */
		//값이 바뀌지 않는 공통 필드 설정된 Builder 생성
		String objectURL = PUBLIC_URL + objectPreSignedURL.getPath() + "?" + objectPreSignedURL.getQuery(); // 좀 더 명시적
		PersonaDTO.Response.PersonaElementDTO.PersonaElementDTOBuilder personaBuilder = PersonaDTO.Response.PersonaElementDTO.builder()
																								 .personaId(persona.getId())
																								 .objectURL(objectURL)
																								 .createdAt(persona.getCreatedAt());
		//작성자가 작성한 글 일경우
		if(persona.getAuthorUuid().equals(userUuid)) {
			MemberDTO.Response.MemberSimple authorMemberSimple= memberMapperService.getMemberSimpl(memberRepository.findById(persona.getAuthorUuid()));
			return personaBuilder.author(authorMemberSimple)
								 .reveal(true)
								 .build();
		}
		
		if(optionalGuessAttempt.isEmpty()) {
			/**
			 * Front와 인터페이스 맞추기위해 익명 MemberSimpleDTO 인스턴스 생성
			 * null로 해도 되는지 아직 확신이 없음 <- 해결 이미 typeScript interface에 union 처리 했어 지장 없었음
			 * 공통 코드이나 적으로 추측을 성공했을 경우에는 필요없기에 각 경우에수 마다 인스턴스 생성하기로 결정
			*/
			MemberDTO.Response.MemberSimple annoMemberSimple = new MemberDTO.Response.MemberSimple(null,persona.getAnonymousName(),null);
			//익명작성자의 personaBuilder build후 리턴
			return personaBuilder.author(annoMemberSimple)
						  .reveal(false)
						  .build();
		}
		/**
		 * 여기서부터
		 * guessAttempt가 존재하는 상태
		 * state1 이용자가 author 추측을 성공한 상태
		 * state2 이용자가 author 추측을 실패 한상태
		 */
		//guessAttempt Optional에서 꺼내욤
		PersonaGuessAttempt guessAttempt =optionalGuessAttempt.get();
		personaBuilder.reveal(true);
		//guessAttemptt success상태로 분기
		if(!guessAttempt.isSuccess()) {
			//추측이 실패 했을 경우
			MemberDTO.Response.MemberSimple annoMemberSimple = new MemberDTO.Response.MemberSimple(null,persona.getAnonymousName(),null);
			//추측에 실패 했지만 attempt 가 존재 할시 annoMemberSimple 넣고 builder build 후 리턴
			return personaBuilder.author(annoMemberSimple).build();
		}
		/**
		 * 추측에 성공한 attempt 존재 => MemberEntity 조회 실제 작성자 리턴
		 */
		MemberDTO.Response.MemberSimple authorMemberSimple= memberMapperService.getMemberSimpl(memberRepository.findById(persona.getAuthorUuid()));
		// 실체가 들어난 작성자 builder build 후 리턴
		return personaBuilder.author(authorMemberSimple).build();
	}
}
