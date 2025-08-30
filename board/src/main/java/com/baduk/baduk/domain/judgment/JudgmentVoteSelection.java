package com.baduk.baduk.domain.judgment;

import java.util.UUID;

import lombok.Getter;

@Getter
public class JudgmentVoteSelection {
	private String id;
	private String title;
	
	public JudgmentVoteSelection(String title) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
	}
	private JudgmentVoteSelection() {}
}