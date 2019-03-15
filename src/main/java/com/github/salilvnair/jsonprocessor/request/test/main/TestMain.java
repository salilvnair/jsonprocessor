package com.github.salilvnair.jsonprocessor.request.test.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.JsonProcessorBuilder;
import com.github.salilvnair.jsonprocessor.request.test.bean.School;
import com.github.salilvnair.jsonprocessor.request.test.bean.Student;

public class TestMain {

	public static void main(String[] args) {
		School school = new School();
		school.setId(1);
		Student student = new Student();
		student.setName("Test A");
		List<Student> students = new ArrayList<>();
		students.add(student);
		//school.setStudents(students);
		Map<String,Object> validatorMap  = new HashMap<>();
		validatorMap.put("test", "Mad");
		List<School> listRequest = new ArrayList<>();
		listRequest.add(school);
		JsonProcessorBuilder jsonProcessorBuilder = new JsonProcessorBuilder();
		List<ValidationMessage>  validationMsgList  = jsonProcessorBuilder
														.request(listRequest)
														.setUserValidatorMap(validatorMap)
														.validate();
		System.out.println(validationMsgList);
	}
	
	
}
