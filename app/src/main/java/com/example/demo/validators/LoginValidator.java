package com.example.demo.validators;

import com.example.demo.dto.LoginDTO;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class LoginValidator implements Validator {
	
    @Override
    public boolean supports(Class<?> clazz) {
        return LoginDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDTO loginDTO = (LoginDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        if (loginDTO.getEmail() != null && (loginDTO.getEmail().length() < 10 || loginDTO.getEmail().length() > 100)) {
            errors.rejectValue("email", "Size.customer.email");
        }

        if (loginDTO.getPassword() != null && (loginDTO.getPassword().length() < 10 || loginDTO.getPassword().length() > 20)) {
            errors.rejectValue("password", "Size.customer.password");
        }
    }
}
