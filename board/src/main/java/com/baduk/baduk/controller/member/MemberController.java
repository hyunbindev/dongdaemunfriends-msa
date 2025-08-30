package com.baduk.baduk.controller.member;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baduk.baduk.data.common.CommonPageDTO;
import com.baduk.baduk.data.member.MemberDTO;
import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;
import com.baduk.baduk.service.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
	private final MemberService memberService;
	
	@GetMapping("/member")
	public ResponseEntity<MemberDTO.Response.MemberDetail> getUserMemberDetailSelf(
			Authentication authentication,
			@RequestParam(name = "redirect", required = false) String redirect) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-Redirect","/blame");
		MemberDTO.Response.MemberDetail responseBody = memberService.getMemberDetail(authentication.getName());
		return ResponseEntity.ok().headers(responseHeaders).body(responseBody);
	}
	
	@GetMapping("/member/{uuid}")
	public ResponseEntity<MemberDTO.Response.MemberDetail> getUserMemberDetail(@PathVariable("uuid") String uuid){
		MemberDTO.Response.MemberDetail responseBody = memberService.getMemberDetail(uuid);
		return ResponseEntity.ok(responseBody);
	}
	
	@GetMapping("/member/search")
	public ResponseEntity<CommonPageDTO<MemberSimple>> getMembersPage(
			@RequestParam(name="name", required = false) String name, 
			@RequestParam(name = "page", defaultValue = "0") int page)
	{
		return name==null ? ResponseEntity.ok(memberService.getAllMeberListPage(page))          //멤버 전체 검색 paageable
				           :ResponseEntity.ok(memberService.getMemberListPage(name, page));     //맴버 이름 으로 검색
	}
}