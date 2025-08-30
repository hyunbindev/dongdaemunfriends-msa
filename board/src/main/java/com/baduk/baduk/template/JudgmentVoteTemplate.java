package com.baduk.baduk.template;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.baduk.baduk.data.common.CommonVoteSelectionResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JudgmentVoteTemplate {
	private final MongoTemplate mongoTemplate;
	private static final String COLLECTION_NAME = "judgmentVote";
	/**
	 * @param judgmentId
	 * @return
	 * 
	 * judgment Selection 별 vote Count 가져오기
	 */
	public List<CommonVoteSelectionResult> getVoteCountFromJudgment(String judgmentId){
		
		//WHERE
		MatchOperation match = Aggregation.match(Criteria.where("judgmentId").is(judgmentId));
		
		//GROUP BY
		GroupOperation group = Aggregation.group("selectionId")
				.first("selectionId").as("selectionId")
				.count().as("voteCount");
		
		Aggregation aggregation = Aggregation.newAggregation(match, group);
		
		AggregationResults<CommonVoteSelectionResult> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, CommonVoteSelectionResult.class);
		return results.getMappedResults();
	}
	/**
	 * @param judgmentIdList
	 * @return
	 * 
	 * judgment 별 total VoteCount 가져오기
	 */
	public Map<String,Integer> getTotalVoteCountFromJudgment(List<String> judgmentIdList){
		MatchOperation match = Aggregation.match(Criteria.where("judgmentId").in(judgmentIdList));
		
	    GroupOperation group = Aggregation.group("judgmentId")
	            .first("judgmentId").as("judgmentId")
	            .count().as("voteCount");
	    
	    Aggregation aggregation = Aggregation.newAggregation(match, group);
	    AggregationResults<Document> results = 
	            mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Document.class);
	    
	    return results.getMappedResults().stream()
	    		.collect(Collectors.toMap(doc-> doc.getString("judgmentId"), doc-> doc.getInteger("voteCount")));
	}
}
