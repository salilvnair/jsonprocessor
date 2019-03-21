package com.github.salilvnair.jsonprocessor.request.test.bean;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;

public class HeadMaster implements JsonRequest {
	@JsonKeyValidator(required=true)
	private String name;
	@JsonKeyValidator(required=true)
	private Integer age;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}	
}