package com.pestov.testexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.TestexerciseApplicationTests;
import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookshelfRepository;
import com.pestov.testexercise.repositories.UserRepository;
import com.pestov.testexercise.services.IBookshelfService;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.util.Arrays.array;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookshelfControllerTest extends TestexerciseApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private IBookshelfService bookshelfService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookshelfRepository bookshelfRepository;
	@Autowired
	private BookRepository bookRepository;

	@Test
	public void submit() throws Exception {
		BookshelfDto bookshelfDto = new BookshelfDto();
		ObjectMapper mapper = new ObjectMapper();
		bookshelfDto.setName("Bookshelf name for unit test. Should be attached to user test1@test.com");

		this.mockMvc.perform(post("/bookshelf")
				.content(mapper.writeValueAsString(bookshelfDto))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", not(isEmptyString())));
	}

	@Test
	public void list() throws Exception {
		this.mockMvc.perform(get("/bookshelf")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath(
						"$.[*].name", Matchers.everyItem(Matchers.isOneOf(
								"Первая полка пользователя test1",
								"Вторая полка пользователя test1",
								"Третья полка пользователя test1"))));
	}

	@Test
	public void getBooksByBookshelf() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/bookshelf/5")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andReturn();

		JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
		JSONObject book1 = jsonArray.getJSONObject(0);
		JSONObject book2 = jsonArray.getJSONObject(1);
		Assert.assertArrayEquals(
				array(book1.getString("name"), book1.getString("description"), book1.getString("bookshelfId")),
				array("Первая книга первой полки юзера test1", "Я заебался фикстуры переделывать", "5"));
		Assert.assertArrayEquals(
				array(book2.getString("name"), book2.getString("description"), book2.getString("bookshelfId")),
				array("Вторая книга первой полки юзера test1", "Убейте меня", "5"));
	}

	@Test
	public void deleteBookshelf() throws Exception {
		Bookshelf bookshelf = new Bookshelf(userRepository.getOne(2L), "Test bookshelf for deleting");
		Long bookshelfId = bookshelfRepository.save(bookshelf).getId();
		Book book = new Book(bookshelf, "Test Book for deleteing", "Desc");
		Long bookId = bookRepository.save(book).getId();

		this.mockMvc.perform(delete("/bookshelf/".concat(bookshelfId.toString()))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());

		Assert.assertEquals(bookRepository.findById(bookId), Optional.empty());
		Assert.assertEquals(bookshelfRepository.findById(bookshelfId), Optional.empty());
	}

	@Test
	public void renameBookshelf() throws Exception {
		String targetName = "RENAMING INITIATED";
		BookshelfDto bookshelfDto = new BookshelfDto();
		ObjectMapper mapper = new ObjectMapper();
		bookshelfDto.setName(targetName);
		bookshelfDto.setId(5L);
		this.mockMvc.perform(put("/bookshelf")
				.content(mapper.writeValueAsString(bookshelfDto))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		Assert.assertEquals(bookshelfService.getBookshelfById(5L).getName(), "RENAMING INITIATED");
	}
}