package com.github.salilvnair.jsonprocessor.request.test.bean;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;

public class Subject {
	@JsonKeyValidator(required=true)
	private String id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
