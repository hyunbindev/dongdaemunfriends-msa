package com.baduk.baduk.repository.blame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.baduk.baduk.domain.blame.BlameComment;

public interface BlameCommentRepository extends MongoRepository<BlameComment,String>{
	Page<BlameComment> findByBlameId(@Param(value="blameId")String blameId, Pageable pageable);
	long countByBlameId(String blameId);
	
}