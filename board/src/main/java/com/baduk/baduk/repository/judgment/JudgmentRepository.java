package com.baduk.baduk.repository.judgment;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.baduk.baduk.domain.judgment.Judgment;

public interface JudgmentRepository extends MongoRepository<Judgment,String>{

}
