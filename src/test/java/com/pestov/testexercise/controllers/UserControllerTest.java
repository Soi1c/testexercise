package com.pestov.testexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.TestexerciseApplicationTests;
import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookSharingRepository;
import com.pestov.testexercise.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest extends TestexerciseApplicationTests {

	@Autowired
	private BookSharingRepository bookSharingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getUsersList() throws Exception {
		this.mockMvc.perform(get("/users")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4)));
	}

	@Test
	public void failingGetUsersListIfUnauthorized() throws Exception {
		this.mockMvc.perform(get("/users"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void getBookshelvesByUser() throws Exception {
		this.mockMvc.perform(get("/users/2")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void getBooksByBookshelfForUser() throws Exception {
		this.mockMvc.perform(get("/users/2/bookshelves/5")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void getBook() throws Exception {
		this.mockMvc.perform(get("/users/2/bookshelves/5/8")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", hasToString("Первая книга первой полки юзера test1")));
	}

	@Test
	public void createBookSharingRequest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		BookSharingDto bookSharingDto = new BookSharingDto();
		bookSharingDto.setOwnerUserId(2L);
		bookSharingDto.setBook_id(5L);
		this.mockMvc.perform(post("/users/booksharingrequest")
		.content(mapper.writeValueAsString(bookSharingDto))
		.contentType(MediaType.APPLICATION_JSON)
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
				.andExpect(status().isOk());
	}

	@Test
	public void getMyRequests() throws Exception {
		createNewBookSharingRequest();
		this.mockMvc.perform(get("/users/getmyrequests")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].bookName", hasToString(bookRepository.getOne(8L).getName())));
	}

	@Test
	public void allowRequestById() throws Exception {
		createNewBookSharingRequest();
		this.mockMvc.perform(put("/users/allowrequest/10")
		.param("expireDate", "2020-01-01")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());
		LocalDate targetDate = LocalDate.of(2020, 1, 1);
		Assert.assertEquals(targetDate, bookSharingRepository.getOne(10L).getExpireDate());
		Assert.assertTrue(bookSharingRepository.getOne(10L).isAllowed());
	}

	@Test
	public void refuseRequestById() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		createNewBookSharingRequest();
		BookSharingDto bookSharingDto = new BookSharingDto();
		bookSharingDto.setRefuseDescription("plz no");
		this.mockMvc.perform(put("/users/refuserequest/10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(bookSharingDto))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());
		Thread.sleep(30000);
		Assert.assertEquals("plz no", bookSharingRepository.getOne(10L).getRefuseDescription());
		Assert.assertFalse(bookSharingRepository.getOne(10L).isAllowed());
	}

	@Test
	public void myRefusedRequests() {
	}

	@Test
	public void mySharedBooks() {
	}

	@Test
	public void getSharedBook() {
	}

	@Test
	public void getSharedPage() {
	}

	@Test
	public void continueReadSharedBook() {
	}

	private void createNewBookSharingRequest() {
		BookSharing bookSharing = new BookSharing();
		bookSharing.setOwnerUser(userRepository.getOne(2L));
		bookSharing.setAskingUser(userRepository.getOne(3L));
		bookSharing.setBook(bookRepository.getOne(8L));
		bookSharingRepository.save(bookSharing);
	}
}