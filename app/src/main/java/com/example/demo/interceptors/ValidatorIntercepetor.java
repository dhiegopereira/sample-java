package com.example.demo.interceptors;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class ValidatorIntercepetor implements HandlerInterceptor {

	private final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String method = request.getMethod();

        if (!"PUT".equals(method) && !"POST".equals(method)) {
        	return true;
        }

        String testHeader = request.getHeader("X-Test-Request");
		boolean isTestRequest = Boolean.parseBoolean(testHeader);

		String className = "";

		if (isTestRequest) {
			className = request.getPathInfo().split("/")[1];
		} else {
			className = request.getServletPath().split("/")[1];
		}
		
		className = className.substring(0, 1).toUpperCase() + className.substring(1);       
		Class<?> clazzDTO = Class.forName("com.example.demo.dto." + className + "DTO");
		Class<?> clazzValidator = Class.forName("com.example.demo.validators." + className + "Validator");

		StringBuilder sb = new StringBuilder();
		String line;		
		try (BufferedReader reader = request.getReader()) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		
		String jsonPayload = sb.toString();
		Object dto = objectMapper.readValue(jsonPayload, clazzDTO);

		DataBinder binder = new DataBinder(dto);
		binder.addValidators((Validator) clazzValidator.getDeclaredConstructor().newInstance());
		binder.validate();
		BindingResult bindingResult = binder.getBindingResult();

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(objectMapper.writeValueAsString(Collections.singletonMap("errors", errors)));
			return false;
		}

		request.setAttribute("DTO", dto);

		return true;
	}
}