package com.jez.blog.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
	private Date dateTime;
    private String message;
    private String details;
}
