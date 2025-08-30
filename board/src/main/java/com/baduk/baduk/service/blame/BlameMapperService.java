package com.baduk.baduk.service.blame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.baduk.baduk.data.blame.BlameCommentDTO;
import com.baduk.baduk.data.blame.BlameDTO;
import com.baduk.baduk.data.member.MemberDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.blame.Blame;
import com.baduk.baduk.domain.blame.BlameComment;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.blame.BlameCommentRepository;
import com.baduk.baduk.service.member.MemberMapperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlameMapperService {
	private final MemberRepository memberRepository;
	private final BlameCommentRepository blameCommentRepository;
	private final MemberMapperService memberMapperService;
	public BlameDTO.Response.Blame mapBlameDTO(Blame blame ,String userUuid){
		//작성자 Entity
		Optional<Member> optionalAuthor = memberRepository.findById(blame.getAuthorUuid());
		//덧글 수 조회
		long blameCommentCount = blameCommentRepository.countByBlameId(blame.getId());
		
		List<MemberDTO.Response.MemberSimple> targetSimpls = new ArrayList<>();
		
		List<Member> targetMembers = memberRepository.findMembersByUuids(blame.getTargetUuids());
		
		for(Member target : targetMembers) {
			targetSimpls.add(memberMapperService.getMemberSimpl(Optional.of(target)));
		}
		
		if(!blame.getTargetUuids().isEmpty() && blame.getTargetUuids().contains(userUuid)) {

					
			MemberDTO.Response.MemberSimple author = new MemberDTO.Response.MemberSimple("secret", "크로코딜로 봄바르딜로", "default");
			
			return BlameDTO.Response.Blame.builder()
					.author(author)
					.id(blame.getId())
					.text("트라랄라레오 트랄라 퉁퉁퉁퉁퉁퉁 사후르 봄바르디로 크로코딜로 보네카 암발라부, 브르르 브르르 파타핌, 심판지니 바나니니, 봄봄비니 구시니, 카푸치노 아사시노, 트리피 트로피, 프리고 카멜로, 라 바카 사투르노 사투르니타")
					.targeted(true)
					.targets(targetSimpls)
					.createdAt(blame.getCreatedAt())
					.commentsCount(blameCommentCount)
					.build();
		}
		
		BlameDTO.Response.Blame blameDTO = BlameDTO.Response.Blame.builder()
				.id(blame.getId())
				.title(blame.getTitle())
				.text(blame.getContent())
				.createdAt(blame.getCreatedAt())
				.commentsCount(blameCommentCount)
				.targeted(false)
				.build();
		
		MemberDTO.Response.MemberSimple authorSimpl= memberMapperService.getMemberSimpl(optionalAuthor);
		
		//작성자및 타겟 설정
		blameDTO.setAuthor(authorSimpl);
		
		//블라인드 처리 되지않은 Blame DTO
		if(!blame.getTargetUuids().isEmpty()) {
			
			blameDTO.setTargets(targetSimpls);
		}
		
		return blameDTO;
	}
	
	public BlameCommentDTO.Response.BlameComment mapBlameCommentDTO(BlameComment blameComment){
		Optional<Member> optionalAuthor = memberRepository.findById(blameComment.getAuthorUuid());
		MemberDTO.Response.MemberSimple authorSimpl = memberMapperService.getMemberSimpl(optionalAuthor);
		
		return BlameCommentDTO.Response.BlameComment.builder()
				.blameCommentId(blameComment.getBlameId())
				.author(authorSimpl)
				.createdAt(blameComment.getCreatedAt())
				.text(blameComment.getText())
				.build();
	}
	

}
