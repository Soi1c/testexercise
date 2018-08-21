package com.pestov.testexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.repositories.RegTokenRepository;
import com.pestov.testexercise.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private RegTokenRepository regTokenRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void submit() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		UserDto userDto = new UserDto();
		userDto.setEmail("fortest@test.com");
		userDto.setPassword("ForTest2!");
		this.mockMvc.perform(post("/signup/submit")
				.content(mapper.writeValueAsString(userDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		Assert.assertTrue(userRepository.findByEmail("fortest@test.com").isPresent());
	}

	@Test
	public void approveUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		UserDto userDto = new UserDto();
		userDto.setEmail("fortest@test.com");
		userDto.setPassword("ForTest2!");
		this.mockMvc.perform(post("/signup/submit")
				.content(mapper.writeValueAsString(userDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		Long createdUserId = userRepository.findByEmail("fortest@test.com").get().getId();
		String token = regTokenRepository.findByCustomUser_Id(createdUserId).getRegToken();
		this.mockMvc.perform(get("/signup/confirmEmail")
				.param("token", token))
				.andExpect(status().isOk());
		Assert.assertTrue(userRepository.getOne(createdUserId).isActive());
	}
}