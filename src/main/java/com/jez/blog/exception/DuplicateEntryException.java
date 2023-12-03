package com.jez.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateEntryException extends RuntimeException {
	private String resourceName;
	private String fieldName;
	private String fieldValue;
	
	public DuplicateEntryException(String resourceName, String fieldName, String fieldValue) {
		super(resourceName+" already exists with "+fieldName+": "+fieldValue);
		
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	
}
