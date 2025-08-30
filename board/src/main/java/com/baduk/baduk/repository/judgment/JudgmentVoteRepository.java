package com.baduk.baduk.repository.judgment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.baduk.baduk.domain.judgment.JudgmentVote;

public interface JudgmentVoteRepository extends MongoRepository<JudgmentVote,String>{
	/**
	 * @param voterUuid
	 * @return
	 */
	Optional<JudgmentVote> findByVoterUuid(String voterUuid);
	/**
	 * 
	 * @param userUuid
	 * @param judgmentId
	 * @return
	 */
	boolean existsByVoterUuidAndJudgmentId(String userUuid, String judgmentId);
	/** 
	 * @param voterUuid
	 * @param judgmentId
	 * @return
	 */
	Optional<JudgmentVote> findByVoterUuidAndJudgmentId(String voterUuid, String judgmentId);
	/**
	 * @param judgmentId
	 * @return
	 * 
	 */
	List<JudgmentVote> findByJudgmentId(String judgmentId);
}
