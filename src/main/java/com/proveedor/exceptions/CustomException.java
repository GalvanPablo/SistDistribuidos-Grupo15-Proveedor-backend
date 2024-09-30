package com.proveedor.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus status;

	public CustomException(String message, String message2, HttpStatus status) {
		super();
		this.message = message;
		this.message = message2;
		this.status = status;
	}

}