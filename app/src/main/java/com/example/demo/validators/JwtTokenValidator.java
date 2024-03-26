package com.example.demo.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenValidator implements Validator {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Override
    public boolean supports(Class<?> clazz) {
        return HttpServletRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            HttpServletRequest request = (HttpServletRequest) target;
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(secret);
                String id = JWT.require(algorithm).build().verify(token.replace("Bearer ", "")).getSubject();
                request.setAttribute("customerId", id);                
            } else {
            	errors.reject("token.invalid", "Token de autenticação ausente");
            }        
        } catch (Exception e) {
            errors.reject("token.invalid", "Token de autenticação inválido");
        }
    }
}
