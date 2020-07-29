package com.github.salilvnair.jsonprocessor.request.type;

public enum Numeric {

	LONG("Long"),
	DOUBLE("Double"),
	INTEGER("Integer"),
	SHORT("Short"),
	FLOAT("Float"),
	DEFAULT("Number");
	
	private String msg;
	
	Numeric(String msg) {
		this.msg = msg;
	}
	
	public String value() {
		return msg;
	}
	
}
