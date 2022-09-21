package com.github.salilvnair.jsonprocessor.request.test.bean;

import com.github.salilvnair.jsonprocessor.request.annotation.ConditionalValidValues;
import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidation;
import com.github.salilvnair.jsonprocessor.request.annotation.ValidValues;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.test.task.HeadMasterTask;
@JsonKeyValidation(id="HeadMaster",customTaskValidator=HeadMasterTask.class)
public class HeadMaster implements JsonRequest {
	@ValidValues(
			conditionalValue = {
				@ConditionalValidValues(value={"X","Y"}, condition="ageGt18"),
				@ConditionalValidValues(value= {"A","B"}, condition="ageLt18"),
			},
			showPredefinedValuesInMessage=true
	)
	@JsonKeyValidation(required=true,customTask="regulate",allowEmpty=true)
	private String name;
	@ValidValues(value= {"10"}, range= {20, 101}, showPredefinedValuesInMessage=true)
	@JsonKeyValidation(required=true,allowNull=true)
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
