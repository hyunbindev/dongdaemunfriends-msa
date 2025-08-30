package com.baduk.baduk.service.member;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.member.MemberDTO;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

public interface MemberService {
	public void signup(MemberDTO.Request.Signup dto);
	
	public void delete(String uuid);
	
	public void changeName(MemberDTO.Request.ChangeName dto);
	
	public void changePosition(MemberDTO.Request.ChangePosition dto);
	
	public MemberDTO.Response.MemberDetail getMemberDetail(String uuid);
	
	public CommonPageDTO<MemberSimple> getMemberListPage(String keyWord ,int page);
	
	public CommonPageDTO<MemberSimple> getAllMeberListPage(int page);
}