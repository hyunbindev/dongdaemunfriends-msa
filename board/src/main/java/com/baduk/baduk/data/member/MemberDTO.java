package com.baduk.baduk.data.member;

import com.baduk.baduk.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberDTO {
	public class Request{
		@Getter
		public static class Signup{
			private String uuid;
			private String name;
			private String profile;
		}
		@Getter
		public static class Delete{
			private String uuid;
		}
		
		@Getter
		public static class ChangeName{
			private String uuid;
			private String name;
		}
		
		@Getter
		public static class ChangePosition{
			private String uuid;
			private String position;
		}
	}
	
	public class Response{
		@Getter
		@Setter
		public static class MemberDetail{
			private String uuid;
			private String name;
			private String profile;
			private Long  coin;
			private Long money;
			private boolean sheriff;
			public MemberDetail(Member entity) {
				this.uuid = entity.getUuid();
				this.name = entity.getName();
				this.profile = entity.getProfile();
				this.coin = entity.getCoin();
				this.money = entity.getMoney();
				this.sheriff = entity.isSheriff();
			}
		}
		
		@Getter
		@Setter
		@Builder
		@AllArgsConstructor
		public static class MemberSimple{
			private String uuid;
			private String name;
			private String profile;
		}
	}
}
