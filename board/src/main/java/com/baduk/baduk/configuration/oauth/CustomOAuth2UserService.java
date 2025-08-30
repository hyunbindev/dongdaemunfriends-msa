package com.baduk.baduk.configuration.oauth;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baduk.baduk.domain.Member;
import com.baduk.baduk.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final MemberRepository memberRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		assignMember(oAuth2User);
		
		return oAuth2User;
	}
	@Transactional
	private Member assignMember(OAuth2User user) {
		String uuid = user.getName();
		
		Optional<Member> optionalMember = memberRepository.findById(uuid);
		Map<String, Object> attributes = user.getAttributes();
		Map<String,Object> properties = (Map<String, Object>)attributes.get("properties");

		String name = (String)properties.get("nickname");
		String profile = (String)properties.get("profile_image");
		//임시 로그
		log.info("{} 접속",name);
		//프로필 사진및 이름 업데이트
		if(optionalMember.isPresent()) {
			Member member = optionalMember.get();
			member.setName(name);
			member.setProfile(profile);
			return memberRepository.save(member);
		}
		//비회원 회원가입 
		Member member =  Member.builder()
				.uuid(uuid)
				.name(name)
				.profile(profile)
				.build();
		
		return memberRepository.save(member);
	}
}
