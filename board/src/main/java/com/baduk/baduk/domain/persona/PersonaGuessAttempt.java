package com.baduk.baduk.domain.persona;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "persona_guess_attempt")
//personaGuessAttempt 두개의 키로 유일 하게 존재해야함
@CompoundIndex(name = "unique_user_target_idx", def = "{'userUuid' : 1, 'targetPersonaId': 1}", unique = true)

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PersonaGuessAttempt {
	@Id
	private String id;
	//시도한 사람
	private String userUuid;
	//시도 persona 아이디
	private String targetPersonaId;
	//제출 내용
	private String submit;
	//성공여부
	private boolean success;
	
	private PersonaGuessAttempt() {};
}