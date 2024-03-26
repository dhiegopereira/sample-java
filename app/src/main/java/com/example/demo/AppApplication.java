package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.demo.interceptors.ValidatorIntercepetor;
import com.example.demo.interceptors.JwtTokenInterceptor;
import com.example.demo.validators.JwtTokenValidator;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	ValidatorIntercepetor validatorIntercepetor() {
		return new ValidatorIntercepetor();
	}

	@Bean
	JwtTokenValidator jwtValidator() {
		return new JwtTokenValidator();
	}

	@Bean
	JwtTokenInterceptor jwtInterceptor(JwtTokenValidator jwtValidator) {
		return new JwtTokenInterceptor(jwtValidator);
	}

	@Bean
	WebMvcConfigurer webMvcConfigurer(ValidatorIntercepetor validatorIntercepetor, JwtTokenInterceptor jwtInterceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(jwtInterceptor);
				registry.addInterceptor(validatorIntercepetor);
			}
		};
	}
}
