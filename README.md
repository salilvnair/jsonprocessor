## JSON PROCESSOR

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
    <artifactId>jsonprocessor</artifactId>
    <version>1.0.5</version>
</dependency>
```

> 2. Annotate a class or a field using  `@JsonKeyValidator ` , class should be implementing the marker interface named `JsonRequest `
```java
@JsonKeyValidator 
public class School implements JsonRequest {
   ....
}
```

> 3. Set it as required, conditional, numeric, email erc.

```java
@JsonKeyValidator(required=true, numeric=true)
private String id;
```

```java
@JsonKeyValidator(conditional=true, condition="validateName")
private String name;
```

```java
@JsonKeyValidator(email=true)
private String email;
```

> 4. Call the processor using `JsonProcessorBuilder`

```java
List<ValidationMessage>  validationMsgList  = new JsonProcessorBuilder()
                                                     .request(school)
                                                     .validate();
```
> 5. User defined map can be passed in the `JsonProcessorBuilder` which can be later used in customTask(s) or in Conditional validators.
		
```java
Map<String,Object> validatorMap  = new HashMap<>();
validatorMap.put("alumini", "Hogward");
List<ValidationMessage>  validationMsgList  = new JsonProcessorBuilder()
                                                     .request(school)
                                                     .setUserValidatorMap(validatorMap)
                                                     .validate();
```
## Complete Usage:
```java
@JsonKeyValidator(id="School", customTaskValidator=SchoolCustomTask.class)
public class School implements JsonRequest {
	@JsonKeyValidator(required=true, message="User defined message!")
	private long id;
	@JsonKeyValidator(conditional=true, condition="validateAlumini")
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
