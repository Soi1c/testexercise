package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.pestov.testexercise.conf.SecurityConstants.HEADER_STRING;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

	private CustomUser user;

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Before
	public void getUser() throws JSONException {
		JSONObject loginDto = new JSONObject();
		loginDto.put("email", "test1@test.com");
		loginDto.put("password", "test");
		String request = loginDto.toString();
		user = userRepository.getOne(new Long(2));
		testRestTemplate.postForObject("http://localhost:".concat(String.valueOf(port)).concat("/login"), request, String.class);
		System.out.print("asdasd");
	}

	@Test
	public void addNewBook() {
		BookDto bookDto = new BookDto();
		bookDto.setBookshelfId(user.getBookshelves().get(0).getId());
		bookDto.setName("One book for test");
		bookDto.setDescription("Test description");

	}

	@Test
	public void updateBook() {
	}

	@Test
	public void deleteBook() {
	}

	@Test
	public void addBookText() {
	}

	@Test
	public void getBook() {
	}

	@Test
	public void getPage() {
	}

	@Test
	public void continueReading() {
	}

	@Test
	public void moveBookToAnotherBookshelf() {
	}
}