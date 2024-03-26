package com.example.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.CustomerDTOConverter;
import com.example.demo.models.Customer;
import com.example.demo.services.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	private final CustomerService customerService;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();   


	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	public ResponseEntity<?> create(HttpServletRequest request) {
		CustomerDTO customerDTO = (CustomerDTO) request.getAttribute("DTO");		
		String passwordCript = encoder.encode(customerDTO.getPassword());
		customerDTO.setPassword(passwordCript);	  
		Customer customer = CustomerDTOConverter.toEntity(customerDTO); Customer
		createdCustomer = customerService.create(customer);	 
		return ResponseEntity.ok(CustomerDTOConverter.toDTO(createdCustomer));
	}

	@GetMapping("/filter")
	public ResponseEntity<?> filter(HttpServletRequest request) {	
		String name = request.getParameter("name");


		List<Customer> customers = customerService.filter(name);
		List<CustomerDTO> customerDTOs = customers.stream()
				.map(CustomerDTOConverter::toDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(customerDTOs); 


	}

	@GetMapping
	public ResponseEntity<?> readOne(HttpServletRequest request) {		
		String customerId = (String) request.getAttribute("customerId");
		Customer cuscomer = customerService.readOne(Long.parseLong(customerId));
		if (cuscomer != null) {
			return ResponseEntity.ok(CustomerDTOConverter.toDTO(cuscomer));
		} else {
			return ResponseEntity.notFound().build();
		}   
	}

	@PutMapping
	public ResponseEntity<?> update(HttpServletRequest request) {
		String customerId = (String) request.getAttribute("customerId");

		CustomerDTO customerDTO = (CustomerDTO) request.getAttribute("DTO");	

		Customer customer = CustomerDTOConverter.toEntity(customerDTO);
		Customer updatedUser = customerService.update(Long.parseLong(customerId), customer);
		if (updatedUser != null) {
			return ResponseEntity.ok(CustomerDTOConverter.toDTO(updatedUser));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping
	public ResponseEntity<?> delete(HttpServletRequest request) {
		String customerId = (String) request.getAttribute("customerId");      

		customerService.delete(Long.parseLong(customerId));
		return ResponseEntity.ok("User deleted successfully");
	}    
}
