package com.example.demo.interceptors;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.validators.JwtTokenValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenInterceptor  implements HandlerInterceptor {
	private final ObjectMapper objectMapper = new ObjectMapper();	
	private final JwtTokenValidator jwtValidator;    


	public JwtTokenInterceptor(JwtTokenValidator jwtValidator) {
		this.jwtValidator = jwtValidator;
	}


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			
		String testHeader = request.getHeader("X-Test-Request");
		boolean isTestRequest = Boolean.parseBoolean(testHeader);

		String method = request.getMethod();
		String router = "";

		if (isTestRequest) {
			router = request.getPathInfo().split("/")[1];
		} else {
			router = request.getServletPath().split("/")[1];
		}
		
		if (("customer".equals(router) && "POST".equals(method)) || "login".equals(router)) {
			return true;
		}
		
		int index = router.indexOf("/");
		if (index != -1) {
			router = router.substring(0, index);
		}		

		Errors validationErrors = new BeanPropertyBindingResult(request, "token");
		jwtValidator.validate(request, validationErrors);

		if (validationErrors.hasErrors()) {
			List<ObjectError> errors = validationErrors.getAllErrors();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(objectMapper.writeValueAsString(Collections.singletonMap("errors", errors)));
			return false;
		}
		String customerId = (String) request.getAttribute("customerId");
		request.setAttribute("customerId", customerId);

		return true;	
	}	
}