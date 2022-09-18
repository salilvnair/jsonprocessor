package com.github.salilvnair.jsonprocessor.request.test.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.helper.JsonValidatorUtil;
import com.github.salilvnair.jsonprocessor.request.service.JsonValidator;
import com.github.salilvnair.jsonprocessor.request.test.bean.HeadMaster;
import com.github.salilvnair.jsonprocessor.request.test.bean.School;
import com.github.salilvnair.jsonprocessor.request.test.bean.Student;
import com.github.salilvnair.jsonprocessor.request.test.bean.Subject;
import com.github.salilvnair.jsonprocessor.request.type.Mode;

public class TestMain {

	public static void main(String[] args) {
		School school = new School();
		school.setId(1);
		school.setTotalStudents("");
		school.setName("Kendriya Vidhayala");
		school.setYear("12/12/2019");
		Student student = new Student();
		student.setName("Test A");
		student.setDateOfAdmission("07/15/2020");
		student.setDateOfBirth("10/10/1990 12:00");
		student.setSection("10C");
		Subject sub = new Subject();
		sub.setName("Maths");
		List<Subject> subs = new ArrayList<>();
		subs.add(sub);
		student.setSubjects(subs);
		ArrayList<Student> students = new ArrayList<>();
		students.add(student);
		HeadMaster headMaster = new HeadMaster();
		headMaster.setAge(20);
		headMaster.setName("Z");
		school.setStudents(students);
		school.setHeadMaster(headMaster);
		school.setName("Bostan 5");

		List<School> schools = new ArrayList<>();
		schools.add(school);


		Map<String,Object> validatorMap  = new HashMap<>();
		validatorMap.put("alumini", "Hogward");

		//Example of calling JsonValidator passing the Object
		JsonValidatorContext jsonValidatorContext = JsonValidatorContext
														.builder()
														.mode(Mode.SYNC)
														.userValidatorMap(validatorMap)
														.build();

		List<ValidationMessage>  validationMsgList  = JsonValidator.validate(school, jsonValidatorContext);
		System.out.println(validationMsgList);

		//Example of calling JsonValidator passing List
		List<ValidationMessage>  validationMsgList1  = JsonValidator.validate(schools, jsonValidatorContext);
		System.out.println(validationMsgList1);
	}
	
	
}
