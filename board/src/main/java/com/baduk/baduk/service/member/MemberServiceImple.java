package com.baduk.baduk.service.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baduk.baduk.constants.excpetion.MemberExceptionConst;
import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.member.MemberDTO.Request.ChangeName;
import com.baduk.baduk.data.member.MemberDTO.Request.ChangePosition;
import com.baduk.baduk.data.member.MemberDTO.Request.Signup;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberDetail;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;
import com.baduk.baduk.domain.Member;
import com.baduk.baduk.exception.MemberException;
import com.baduk.baduk.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImple implements MemberService{
	private final MemberRepository memberRepository;
	
	@Override
	@Transactional
	public void signup(Signup dto) {
		Member member = Member.builder()
				.uuid(dto.getUuid())
				.name(dto.getName())
				.profile(dto.getProfile())
				.build();
		memberRepository.save(member);
	}

	@Override
	public void delete(String uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public void changeName(ChangeName dto) {
		Member member = memberRepository.findById(dto.getUuid())
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		member.setName(dto.getName());
	}
	
	@Override
	@Transactional
	public void changePosition(ChangePosition dto) {
		Member member = memberRepository.findById(dto.getUuid())
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		member.setPosition(dto.getPosition());
	}

	@Override
	@Transactional(readOnly = true)
	public MemberDetail getMemberDetail(String uuid) {
		Member member = memberRepository.findById(uuid)
				.orElseThrow(()-> new MemberException(MemberExceptionConst.NO_MEMBER));
		
		return new MemberDetail(member);
	}

	@Override
	@Transactional(readOnly = true)
	public CommonPageDTO<MemberSimple> getMemberListPage(String keyWord ,int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "name"));
		Page<Member> memberPage = memberRepository.findByNameContaining(keyWord, pageable);
		
		return new CommonPageDTO<MemberSimple>("members", memberPage, 
				member -> new MemberSimple(member.getUuid(), 
						                   member.getName(), 
						                   member.getProfile()));
	}
	
	@Override
	@Transactional(readOnly = true)
	public CommonPageDTO<MemberSimple> getAllMeberListPage(int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "name"));
		Page<Member> memberPage = memberRepository.findAll(pageable);
		
		return new CommonPageDTO<MemberSimple>("members", memberPage,
				member -> new MemberSimple(member.getUuid(),
						                   member.getName(),
						                   member.getProfile()));
	}
}
