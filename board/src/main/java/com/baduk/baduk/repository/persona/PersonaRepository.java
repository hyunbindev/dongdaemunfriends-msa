package com.baduk.baduk.repository.persona;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.baduk.baduk.domain.persona.Persona;

public interface PersonaRepository extends MongoRepository<Persona,String>{

}