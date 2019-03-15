package com.github.salilvnair.jsonprocessor.request.test.bean;

import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;


public class Student extends JsonRequest{
	@JsonKeyValidator(required=true)
	private String name;
	@JsonKeyValidator(alphaNumeric=true,allowNull=true)
	private String section;
	@JsonKeyValidator(required=true)
	private String gender;
	@JsonKeyValidator(required=true)
	private List<Subject> subjects;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
}
