package com.example.demo.controllers;



import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/teste")
public class SampleController {
    
    @PostMapping
    public String sample() {

    	
    	
    	return "Exemplo de API Spring boot com ID: ";
    }
}
