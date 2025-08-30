package com.baduk.baduk.service.blame;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baduk.baduk.constants.excpetion.BlameExceptionConst;
import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.data.blame.BlameDTO;
import com.baduk.baduk.data.blame.BlameDTO.Request.CreateBlame;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.domain.blame.Blame;
import com.baduk.baduk.exception.BlameException;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.repository.MemberRepository;
import com.baduk.baduk.repository.blame.BlameRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hyunbinDev
 * @since 2025-06-25
 * 단일 저격대상에서 복수 저격대상으로 전환
 * 
 * @since 2025-08-24
 * notification 기능 추가
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BlameServiceImpl implements BlameService{
	private final BlameRepository blameRepository;
	private final MemberRepository memberRepository;
	private final BlameMapperService blameMapperService;
	private final BlameNotificationService blameNotificationService;
	/**
	 * @author hyunbinDev
	 * @since 2025-06-25
	 * @implNote 저격글 생성
	 * 
	 * @since 2025-08-24
	 * 저격글 작성시 notification 기능 추가
	 */
	@Override
	@Transactional
	public BlameDTO.Response.CreateBlame createBlame(CreateBlame dto) {
		Member author = memberRepository.findById(dto.getAuthorUuid())
				.orElseThrow(()->new MemberException(MemberExceptionConst.NO_MEMBER));
		
		
		Blame blame = Blame.builder()
				.title(dto.getTitle())
				.authorUuid(author.getUuid())
				//타겟이 단일 String에서 List로 전환
				.targetUuids(dto.getTargetUuids())
				.content(dto.getContent())
				.build();
		
			/**
			 * if(!dto.getTargetUuids().isEmpty()) {
			 *  Member target = memberRepository.findById(dto.getTargetUuid())
			 *		.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER, "저격대상이 존재하지 않습니다."));
			 *
			 *	blame.setTargetUuid(target.getUuid());
			 *  @REFECT 저격글 생성시 기존 단일 저격대상에서 복수 저격대상으로 전환
			 *  
			 *  한번의 쿼리로 리스트에 들어있는 저격대상 조회 필요
			 *  }
			 */
		
		blame = blameRepository.save(blame);
		
		//Notification event 발행
		blameNotificationService.sendBlameTargetNotification(blame);
		
		return new BlameDTO.Response.CreateBlame(blame.getId());
	}
	
	@Override
	@Transactional
	public void deleteBlame(String userUuid , String blameId) {
		//작성자 조회
		Member author = memberRepository.findById(userUuid)
				.orElseThrow(()->new MemberException(MemberExceptionConst.NO_MEMBER,"잘못된 접근입니다."));
		//저격글 조회
		Blame blame = blameRepository.findById(blameId)
				.orElseThrow(()-> new BlameException(BlameExceptionConst.NO_BLAME,"잘못된 저격글 접근 입니다."));
		
		//소유권한 확인
		if(!author.getUuid().equals(blame.getAuthorUuid())) 
				throw new MemberException(MemberExceptionConst.NO_AUTHORITY,"삭제권한이 없습니다.");
		
		//삭제 처리
		blameRepository.delete(blame);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BlameDTO.Response.Blame getBlame(String usesrUuid ,String blameId ,Authentication authentication) {
		Blame blame = blameRepository.findById(blameId)
				.orElseThrow(()-> new BlameException(BlameExceptionConst.NO_BLAME));
		
		//저격 대상일 시 블라인드처리된 저격글 리턴
		if(!blame.getTargetUuids().isEmpty() && blame.getTargetUuids().contains((authentication.getName()))) {
			/**
			 * @REFECT
			 * @since 2025-06-05
			 * throw new MemberException(MemberExceptionConst.NO_AUTHORITY); 401 리턴하지말고 블라인드 처리된 저격글 리턴
			 */
			return blameMapperService.mapBlameDTO(blame, authentication.getName());
		}
		//정상 조회된 저격글 리턴
		return blameMapperService.mapBlameDTO(blame , authentication.getName());
	}
	
	@Override
	@Transactional(readOnly = true)
	public CommonPageDTO<BlameDTO.Response.Blame> getBlameList(int page ,Authentication authentication) {
		Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<Blame> blamePage = blameRepository.findAll(pageable);
		
		return new CommonPageDTO<BlameDTO.Response.Blame>("blames", blamePage, blame -> blameMapperService.mapBlameDTO(blame ,authentication.getName()));
	}
}