package com.baduk.baduk.repository.persona;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.baduk.baduk.domain.persona.PersonaGuessAttempt;

public interface PersonaGuessAttemptRepository extends MongoRepository<PersonaGuessAttempt,String>{

	Optional<PersonaGuessAttempt> findByUserUuidAndTargetPersonaId(String userUuid, String targetPersonaId);

}
