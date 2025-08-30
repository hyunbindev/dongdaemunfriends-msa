package com.baduk.baduk.domain.persona;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "persona")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Persona {
	@Id
	private String id;
	
	//익명의 이름
	private String anonymousName;
	
	//실제 작성자 uuid
	private String authorUuid;
	
	//목소리 오브젝트 key
	private String objectKey;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private Persona() {};
}