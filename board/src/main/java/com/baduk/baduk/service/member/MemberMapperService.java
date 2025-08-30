package com.baduk.baduk.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.baduk.baduk.data.member.MemberDTO;
import com.baduk.baduk.domain.Member;

@Service
public class MemberMapperService {
	public MemberDTO.Response.MemberSimple getMemberSimpl(Optional<Member> optionalMember){
		if(optionalMember.isPresent()) {
			Member member = optionalMember.get();
			return new MemberDTO.Response.MemberSimple(member.getUuid(), member.getName(), member.getProfile());
		}
		return new MemberDTO.Response.MemberSimple(null,"탈주 사용자",null);
	}
}
