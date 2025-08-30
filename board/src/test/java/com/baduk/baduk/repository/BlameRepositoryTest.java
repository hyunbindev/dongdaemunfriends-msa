package com.baduk.baduk.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.baduk.baduk.domain.blame.Blame;
import com.baduk.baduk.repository.blame.BlameRepository;

@DataMongoTest
@Disabled
@DisplayName("Mongo DB TEST")
public class BlameRepositoryTest {
	@Autowired
	private BlameRepository repo;
	@Test
	@DisplayName("Blame 저장 테스트")
	void saveBlameTest() {
		Blame testBlame = Blame.builder()
				.title("blame test title")
				.build();
		Blame res = repo.save(testBlame);
		assertThat(res).isEqualTo(testBlame);
	}
	
	@Test
	@DisplayName("Blame Update Test")
	void updateTest() {
		Blame blame = Blame.builder()
				.title("test")
				.build();
		Blame res = repo.save(blame);
		
		res.setTitle("changeTest");
		
		repo.save(res);
		
		Blame changedRes = repo.findById(res.getId())
				.orElseThrow();
		
		assertThat(res.getTitle()).isEqualTo(changedRes.getTitle());
	}
}
