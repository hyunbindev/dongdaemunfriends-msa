package com.baduk.baduk.repository.blame;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.baduk.baduk.domain.blame.Blame;

public interface BlameRepository extends MongoRepository<Blame,String>{
	Page<Blame> findAll(Pageable pageable);
}