package com.example.demo.controllers;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 
public class CustomerControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String authToken;

	@Test
	@Order(1)
	public void testCreateCustomer() throws Exception {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setName("Ana Paula");
		customerDTO.setEmail("anapaula@gmail.com");
		customerDTO.setPassword("12345678910");

		String customerDTOJson = objectMapper.writeValueAsString(customerDTO);

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/customer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerDTOJson).header("X-Test-Request", "true"));

		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ana Paula"));
		result.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("anapaula@gmail.com"));
	} 

	@Test
	@Order(2)
	public void testAuth() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmail("anapaula@gmail.com");
		loginDTO.setPassword("12345678910");

		String loginDTOJson = new ObjectMapper().writeValueAsString(loginDTO);

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginDTOJson).header("X-Test-Request", "true"));


		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
		result.andDo(mvcResult -> {
			String response = mvcResult.getResponse().getContentAsString();
			JSONObject jsonResponse = new JSONObject(response);
			authToken = jsonResponse.getString("token");
		});
	}

	@Test
	@Order(3)
	public void testFilterAll() throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/filter")
				.header("X-Test-Request", "true")
				.header("Authorization", "Bearer " + authToken));
		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
		result.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
	}	


	@Test
	@Order(4)
	public void testReadOne() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/customer")
				.header("X-Test-Request", "true")
				.header("Authorization", "Bearer " + authToken))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@Order(5)
	public void testUpdate() throws Exception {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setName("Ana Paula Coreaú");
		customerDTO.setEmail("anapaula@gmail.com");
		customerDTO.setPassword("12345678910");

		String customerDTOJson = objectMapper.writeValueAsString(customerDTO);

		mockMvc.perform(MockMvcRequestBuilders.put("/customer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customerDTOJson)
				.header("X-Test-Request", "true")
				.header("Authorization", "Bearer " + authToken)) 
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@Order(6)
	public void testDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/customer")
				.header("X-Test-Request", "true")
				.header("Authorization", "Bearer " + authToken)) 
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

}