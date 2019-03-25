package com.github.salilvnair.jsonprocessor.request.test.bean;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.test.task.SomeCustomTask;
@JsonKeyValidator(customTaskValidator=SomeCustomTask.class)
public class Subject implements JsonRequest{
	@JsonKeyValidator(conditional = true, condition="someRandomCondition")
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
