## JSON VALIDATOR

> Easy annotation based validator.

> Predefined validations right out of the box i.e. required, minItems, maxItems, email, numeric etc.

> Supports dynamic validations using conditional, customTask(s).

> Supports ValidValues annotation for set of valid values.

> Can be used with any framework of Java or a Rest based Java API.

> A user validator map, user defined message set, valid value data set can be configured using builder pattern.

>  Output of the validation contains type and a json path(even the complex one) to rightly identify the issues.

> Pass an Object Instance or List instance directly to the validation util.

## Steps:
> 1. Add Maven dependency

```java
<dependency>
    <groupId>com.github.salilvnair</groupId>
    <artifactId>json-validator</artifactId>
    <version>1.0.1</version>
</dependency>
```

> 2. Annotate a class or a field using  `@JsonKeyValidation ` , class should be implementing the marker interface named `JsonRequest `
```java
@JsonKeyValidation 
public class School implements JsonRequest {
   ....
}
```

> 3. Set it as required, conditional, numeric, email erc.

```java
@JsonKeyValidation(required=true, numeric=true)
private String id;
```

```java
@JsonKeyValidation(conditional=true, condition="validateName")
private String name;
```

```java
@JsonKeyValidation(email=true)
private String email;
```

> 4. Call the validator by passing the JsonValidatorContext using `JsonValidator`

```java

JsonValidatorContext jsonValidatorContext = JsonValidatorContext
                                                .builder()
                                                .build();

List<ValidationMessage>  validationMsgList  = JsonValidator.validate(school, jsonValidatorContext);
```
> 5. User defined map can be passed in the builder which can be later used in customTask(s) or in Conditional validators.
		
```java

Map<String,Object> validatorMap  = new HashMap<>();
validatorMap.put("alumini", "Hogward");

JsonValidatorContext jsonValidatorContext = JsonValidatorContext
                                                .builder()
                                                .userValidatorMap(validatorMap).build();

List<ValidationMessage>  validationMsgList  = JsonValidator.validate(school, jsonValidatorContext);
```
## Complete Usage:
```java
@JsonKeyValidation(id="School", customTaskValidator=SchoolCustomTask.class)
public class School implements JsonRequest {
	@JsonKeyValidation(required=true, message="User defined message!")
	private long id;
	@JsonKeyValidation(conditional=true, condition="validateAlumini")
	private String name;
	@JsonKeyValidation(required=true)
	private HeadMaster headMaster;
	@JsonKeyValidation(
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
						message="Minimum 4 students should be there at "+JsonKeyValidationConstant.PATH_PLACEHOLDER,
						messageType = MessageType.WARNING
				)
		}
	)
	private List<Student> students;
}
```
```java
public class SchoolCustomTask extends AbstractCustomJsonValidatorTask {
	public String validateAlumini(JsonValidatorContext jsonValidatorContext) {
		School school = (School) jsonValidatorContext.getJsonRequest();
		Map<String,Object> validatorMap = jsonValidatorContext.getUserValidatorMap();
		if(!validatorMap.isEmpty() && school.getName()!=null) {
			String alumini  = (String) validatorMap.get("alumini");
			if(!school.getName().contains(alumini)){
				return "Only "+alumini+" alumini schools are allowed";
			}
		}
		return null;
	}
}
```
