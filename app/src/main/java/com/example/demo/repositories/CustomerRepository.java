package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Customer findByName(String name);
	Customer findByEmail(String email);
    List<Customer> findByNameContainingIgnoreCase(String name);
}