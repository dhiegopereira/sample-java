package com.example.demo.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.dto.CustomerDTO;

import jakarta.servlet.http.HttpServletRequest;

public class CustomerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerDTO.class.equals(clazz);
	}
 	
	@Override
	public void validate(Object target, Errors errors) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String method = request.getMethod();
		
		if ("GET".equals(method) || "POST".equals(method)) {
			CustomerDTO customerDTO = (CustomerDTO) target;

			if ((customerDTO.getName() != null || customerDTO.getName() == "") && (customerDTO.getName().length() < 2 || customerDTO.getName().length() > 20)) {
				errors.rejectValue("name", "The name field cannot be null or empty, it must be between 2 and 20 characters long");
			}

			if (customerDTO.getEmail() != null && (customerDTO.getEmail().length() < 10 || customerDTO.getEmail().length() > 100)) {
				errors.rejectValue("email", "The email field cannot be null or empty, it must be between 20 and 100 characters long");
			}

			if (customerDTO.getPassword() != null && (customerDTO.getPassword().length() < 10 || customerDTO.getPassword().length() > 20)) {
				errors.rejectValue("password", "The password field cannot be null or empty, it must be between 10 and 20 characters long");
			}
		}
	}
}
