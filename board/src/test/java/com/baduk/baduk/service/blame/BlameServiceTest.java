package com.baduk.baduk.service.blame;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baduk.baduk.data.blame.BlameDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.blame.Blame;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.blame.BlameCommentRepository;
import com.baduk.baduk.repository.blame.BlameRepository;

/**
 * @since 2025-06-11
 * 중복저격대상으로 코드 변경에 따라 단위테스트 수정
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BlameService Test")
public class BlameServiceTest {
	@Mock
	MemberRepository memberRepository;
	@Mock
	BlameRepository blameRepository;
	@Mock
	BlameCommentRepository blameCommentRepository;
	
	@InjectMocks
	BlameServiceImpl blameService;
	
	@Test
	@DisplayName("Blame create Test")
	void createBlameTest() {
		//givenDTO
		
		//target uuid list
		List<String> targetUuids = new ArrayList<>();
		
		for(int i =0; i<150; i++) {
			targetUuids.add("target_uuid_"+i);
		}
		
		//createDTO
		BlameDTO.Request.CreateBlame createBlameDTO = new BlameDTO.Request.CreateBlame();
		createBlameDTO.setAuthorUuid("author_uuid");
		createBlameDTO.setContent("blame text content");
		createBlameDTO.setTargetUuids(targetUuids);
		createBlameDTO.setTitle("title");
		
		blameService.createBlame(createBlameDTO);
		
		Optional<Member> author = Optional.of(Member.builder().uuid(createBlameDTO.getAuthorUuid()).build());
		
		//when
		when(memberRepository.findById(createBlameDTO.getAuthorUuid())).thenReturn(author);
		
		
		verify(blameRepository).save(any(Blame.class));
	}
}