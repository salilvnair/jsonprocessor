package com.github.salilvnair.jsonprocessor.request.context;

import com.github.salilvnair.jsonprocessor.request.type.MessageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ValidationMessage {
	
    private MessageType type;
    private String path;
    private String message;
    
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	
    
}
