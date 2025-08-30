package com.baduk.baduk.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.baduk.baduk.domain.Member;

@SpringBootTest
@Transactional
@Disabled
@Rollback
public class MemberRepositoryTest {
	@Autowired
	private MemberRepository repo;
	String uuid = "uuid";
	@Test
	@DisplayName("Member save Test")
	public void memberSaveTest() {
		//given
		
		Member member = Member.builder()
				.uuid(uuid)
				.name("name")
				.profile("profileURL")
				.build();
		//when
		Member result = repo.save(member);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result.getUuid()).isEqualTo(uuid);
		assertThat(result.getCoin()).isEqualTo(1000L);
	}
	@Test
	@DisplayName("Member delete Test")
	public void memberDeleteTest() {
		Member member = Member.builder()
				.uuid(uuid)
				.name("name")
				.profile("profileURL")
				.build();
		//when
		Member result = repo.save(member);
		Member target = repo.findById(result.getUuid())
				.orElseThrow();
		repo.delete(target);
	}
	@Test
	@DisplayName("Change Member Name")
	@Transactional
	public void changeMemberName() {
		Member member = Member.builder()
				.uuid(uuid)
				.name("name")
				.profile("profileURL")
				.build();
		repo.save(member);
		Member target = repo.findById(uuid)
				.orElseThrow();
		target.setName("changeName");
		
		Member changedMember = repo.findById(uuid)
				.orElseThrow();
		
		assertThat(changedMember.getName()).isEqualTo("changeName");
	}
}