package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.CustomerController;
import com.example.demo.models.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerController.class)
class CustomerControllerTests {

	  @Autowired
	    private MockMvc mockMvc;

	    @Autowired
	    private ObjectMapper objectMapper;

	    @Test
	    void testCreateCustomer() throws Exception {
	        Customer customer = new Customer(); // Create a sample Customer object
	        
	        customer.setName("Ana Paula");
	        customer.setEmail("anapaula@gmail.com");
	        customer.setPassword("12345678910");

	        mockMvc.perform(post("/customer")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(customer)))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.id").exists()); // Assuming your DTO returns an "id" field
	    }

}
