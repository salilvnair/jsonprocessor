package com.github.salilvnair.jsonprocessor.request.test.bean;

import java.util.ArrayList;
import java.util.List;

import com.github.salilvnair.jsonprocessor.request.annotation.JsonKeyValidator;
import com.github.salilvnair.jsonprocessor.request.annotation.UserDefinedMessage;
import com.github.salilvnair.jsonprocessor.request.constant.JsonKeyValidatorConstant;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.test.task.SchoolCustomTask;
import com.github.salilvnair.jsonprocessor.request.type.MessageType;
import com.github.salilvnair.jsonprocessor.request.type.ValidatorType;
@JsonKeyValidator(customTaskValidator=SchoolCustomTask.class)
public class School implements JsonRequest{
	@JsonKeyValidator(required=true)
	private long id;
	@JsonKeyValidator(conditional=true,condition="someRandomCondition")
	private String name;
	@JsonKeyValidator(required=true)
	private HeadMaster headMaster;
	@JsonKeyValidator(
		required=true,
		minItems=4,		
		userDefinedMessages = {
				@UserDefinedMessage(
						validatorType=ValidatorType.REQUIRED,
						message="Students are mandatory in a school",
						messageType=MessageType.ERROR
				),
				@UserDefinedMessage(
						validatorType=ValidatorType.MINITEMS,
						message="Minimum 4 students should be there at "+JsonKeyValidatorConstant.PATH_PLACEHOLDER,
						messageType = MessageType.WARNING
				)
		}
	)
	private ArrayList<Student> students;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}
	public HeadMaster getHeadMaster() {
		return headMaster;
	}
	public void setHeadMaster(HeadMaster headMaster) {
		this.headMaster = headMaster;
	}
}