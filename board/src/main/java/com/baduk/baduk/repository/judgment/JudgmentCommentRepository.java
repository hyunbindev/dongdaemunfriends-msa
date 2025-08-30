package com.baduk.baduk.repository.judgment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.baduk.baduk.domain.judgment.JudgmentComment;

public interface JudgmentCommentRepository extends MongoRepository<JudgmentComment,String>{
	Page<JudgmentComment> findByJudgmentId(@Param(value="judgmentId")String judgmentId, Pageable pageable);
	long countByJudgmentId(String judgmentId);
}
