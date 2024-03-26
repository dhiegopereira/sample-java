package com.example.demo.dto;

import com.example.demo.models.Customer;

public class CustomerDTOConverter {
	  public static CustomerDTO toDTO(Customer customer) {
	        CustomerDTO customerDTO = new CustomerDTO();
	        customerDTO.setName(customer.getName());
	        customerDTO.setEmail(customer.getEmail());
	        customerDTO.setPassword(customer.getPassword());
	        return customerDTO;
	    }

	    public static Customer toEntity(CustomerDTO customerDTO) {
	        Customer customer = new Customer();
	        customer.setName(customerDTO.getName());
	        customer.setEmail(customerDTO.getEmail());
	        customer.setPassword(customerDTO.getPassword());
	        return customer;
	    }
}
