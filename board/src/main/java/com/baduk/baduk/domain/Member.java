package com.baduk.baduk.domain;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member {
	@Id
	private String uuid;
	
	@Column(nullable =false)
	private String name;
	
	private String profile;
	
	@Column(nullable =false)
	@ColumnDefault("1000")
	private Long coin = 1000L;
	
	@Column(nullable =false)
	private Long money = 0L;
	
	@Column(nullable = false)
	private String position = "";
	
	@Column(nullable = false)
	private boolean sheriff = false;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMoney(Long money) {
		this.money = money;
	}
	
	public void setCoin(Long coin) {
		this.coin = coin;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	public void setSheriff(boolean sheriff) {
		this.sheriff = sheriff;
	}
	
	private Member(Builder builder) {
		this.uuid = builder.uuid;
		this.name = builder.name;
		this.profile = builder.profile;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder{
		private String uuid;
		private String name;
		private String profile;
		
		public Builder uuid(String uuid) {
			this.uuid = uuid;
			return this;
		}
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder profile(String profile) {
			this.profile = profile;
			return this;
		}
		public Member build() {
			return new Member(this);
		}
	}
}