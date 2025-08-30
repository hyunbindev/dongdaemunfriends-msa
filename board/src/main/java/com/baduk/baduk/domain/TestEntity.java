package com.baduk.baduk.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestEntity {
	@Id
	private String name;
}
