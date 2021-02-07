package com.tools.models;

public class ResponseModel {

	private String Message;
	
	private Object responseObject;
	
	private boolean isErrorFound = false;
	
	private String MessageType = "";

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public Object getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public boolean isErrorFound() {
		return isErrorFound;
	}

	public void setErrorFound(boolean isErrorFound) {
		this.isErrorFound = isErrorFound;
	}

	public String getMessageType() {
		return MessageType;
	}

	public void setMessageType(String messageType) {
		MessageType = messageType;
	}

}
