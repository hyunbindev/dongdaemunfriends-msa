package com.baduk.baduk.data.persona;

import java.time.LocalDateTime;

import com.baduk.baduk.data.member.MemberDTO.Response.MemberSimple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hyunbinDev
 * @since 2025-06-16
 * 페르소나 DTO
 */
public class PersonaDTO {
	//요청 DTO
	public class Request{
		//*********************************************************************************
		/**
		 * @author hyunbinDev
		 * @since 2025-06-18
		 * PersonaGuess Request DTO
		 * 
		 */
		@Getter
		public static class PersonaGuessDTO{
			private String personaId;
			private String guessAuthorUuid;
		}
		//*********************************************************************************
	}
	//응답 DTO
	public class Response{
		//*********************************************************************************
		@Getter
		@Setter
		@Builder
		@AllArgsConstructor
		public static class PersonaElementDTO{
			private String personaId;
			private MemberSimple author;
			private boolean reveal;
			private LocalDateTime createdAt;
			private String objectURL;
		}
		//*********************************************************************************
		@Getter
		/**
		 * @author hyunbinDev
		 * @since 2025-06-18
		 * PersonaGuess Response DTO
		 * 객체 불변을 지키기 위해 setter 제거
		 * 경우가 두가지 강제됨 두가지 경우 static method로 강제 하여 실수 방지
		 */
		public static class PersonaGuessDTO{
			private boolean success;
			private MemberSimple author;
			//생성자 직접 호출 방지
			private PersonaGuessDTO(boolean success, MemberSimple author) {
				this.success = success;
				this.author = author;
			}
			//생성자 직접 호출 방지
			private PersonaGuessDTO() {}
			
			//추측 실패시
			public static PersonaGuessDTO getFailGuessDTO() {
				return new PersonaGuessDTO(false, null);
			}
			//추측 성공시
			public static PersonaGuessDTO getSuccessGuessDTO(MemberSimple author) {
				return new PersonaGuessDTO(true, author);
			}
		}
		//*********************************************************************************
	}
}