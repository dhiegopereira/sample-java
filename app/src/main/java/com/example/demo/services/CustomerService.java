package com.example.demo.services;

import com.example.demo.models.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.CustomerRepository;

@Service
public class CustomerService {
	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public List<Customer> filter(String name) {
		if (name != null && !name.isEmpty()) {	        
			return customerRepository.findByNameContainingIgnoreCase(name);
		} else {	        
			return customerRepository.findAll();
		}	        
	}

	public Customer readOne(Long id) {
		Optional<Customer> userOptional = customerRepository.findById(id);
		return userOptional.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}

	public Customer create(Customer customer) {
		if (customerRepository.findByName(customer.getName()) != null) {
			throw new IllegalArgumentException("Já existe um usuário com esse nome");
		}
		if (customerRepository.findByEmail(customer.getEmail()) != null) {
			throw new IllegalArgumentException("Já existe um usuário com esse email");
		}
		return customerRepository.save(customer);
	}

	public Customer update(Long id, Customer customer) {
		if (!customerRepository.existsById(id)) {
			throw new RuntimeException("User not found with id: " + id);
		}
		customer.setId(id);
		return customerRepository.save(customer);
	}

	public void delete(Long id) {
		try {
			customerRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new RuntimeException("User not found with id: " + id);
		}
	}

	public Customer findByEmail(String email) {
		try {
			Customer customer = customerRepository.findByEmail(email);    	  
			return customer;
		} catch (Exception e) {
			throw new RuntimeException("Email or password not found");
		}

	} 
}
