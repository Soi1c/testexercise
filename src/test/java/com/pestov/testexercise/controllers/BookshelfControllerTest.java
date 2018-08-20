package com.pestov.testexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.TestexerciseApplication;
import com.pestov.testexercise.TestexerciseApplicationTests;
import com.pestov.testexercise.conf.H2ServerConfiguration;
import com.pestov.testexercise.conf.JWTAutheticationFilter;
import com.pestov.testexercise.conf.JWTAuthorizationFilter;
import com.pestov.testexercise.conf.SecurityConstants;
import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.services.IBookService;
import com.pestov.testexercise.services.IBookshelfService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestexerciseApplication.class)
@WebMvcTest(BookshelfController.class)
@AutoConfigureMockMvc
public class BookshelfControllerTest extends TestexerciseApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IBookshelfService bookshelfService;

	@Autowired
	private IBookService bookService;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void submit() throws Exception {
		BookshelfDto bookshelfDto = new BookshelfDto();
		ObjectMapper mapper = new ObjectMapper();
		bookshelfDto.setName("Bookshelf name for unit test. Should be attached to user test1@test.com");

		this.mockMvc.perform(post("/bookshelf")
				.content(mapper.writeValueAsString(bookshelfDto))
				.characterEncoding("UTF-8")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());
	}

	@Test
	public void list() {
	}

	@Test
	public void getBooksByBookshelf() {
	}

	@Test
	public void deleteBookshelf() {
	}

	@Test
	public void renameBookshelf() {
	}
}