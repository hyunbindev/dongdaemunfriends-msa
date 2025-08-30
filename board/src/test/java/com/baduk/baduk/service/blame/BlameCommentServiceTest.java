package com.baduk.baduk.service.blame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.blame.BlameCommentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlameCommentService Test")
public class BlameCommentServiceTest {
	@Mock
	BlameCommentRepository blameCommentRepository;
	@Mock
	MemberRepository memberRepository;
	
	@InjectMocks
	BlameCommentServiceImpl blameCommentService;
	
	@Test
	@DisplayName("Blame comment Page test")
	void blameCommentPage() {
		//given
		
		//when
		
		//then
		
	}
}
