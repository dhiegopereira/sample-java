package com.example.demo.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.LoginDTO;
import com.example.demo.models.Customer;
import com.example.demo.services.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

	private final CustomerService customerService;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    

	@Value("${spring.jwt.secret}")
	private String secret;


	public LoginController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@PostMapping()
	public ResponseEntity<Map<String, String>> auth(HttpServletRequest request) {

		LoginDTO loginDTO = (LoginDTO) request.getAttribute("DTO");
		Algorithm algorithm = Algorithm.HMAC256(secret);

		Customer customer = customerService.findByEmail(loginDTO.getEmail());         

		boolean result = encoder.matches(loginDTO.getPassword(), customer.getPassword());

		if (result) {        	  
			String token = JWT.create()
					.withSubject(customer.getId().toString())
					.withClaim("name", customer.getName()) 
					.withClaim("email", customer.getEmail())
					.sign(algorithm);
			Map<String, String> response = new HashMap<>();	
			response.put("token", token);

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.ok(Collections.singletonMap("error", "Email or password not found!"));
		}
	} 

}
