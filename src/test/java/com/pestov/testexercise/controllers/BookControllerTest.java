package com.pestov.testexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.TestexerciseApplicationTests;
import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookshelfRepository;
import com.pestov.testexercise.repositories.PageRepository;
import com.pestov.testexercise.services.IBookshelfService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerTest extends TestexerciseApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private IBookshelfService bookshelfService;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookshelfRepository bookshelfRepository;
	@Autowired
	private PageRepository pageRepository;


	@Test
	public void addNewBook() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		BookDto bookDto = new BookDto();
		bookDto.setBookshelfId(5L);
		bookDto.setName("Тестовая книга для первой полки юзера test1@test.com");
		bookDto.setDescription("Дескриптион");
		JSONObject response = new JSONObject(this.mockMvc.perform(post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(bookDto))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", not(isEmptyString())))
				.andReturn().getResponse().getContentAsString());
		Long id = response.getLong("id");
		bookRepository.deleteById(id);
	}

	@Test
	public void failingAddingNewBookToForeignBookshelf() throws Exception {
		BookshelfDto bookshelfDto = new BookshelfDto();
		bookshelfDto.setName("Полка для другого юзера");
		Long foreignShelfId = bookshelfService.saveNewBookshelf(bookshelfDto, 4L).getId();
		ObjectMapper mapper = new ObjectMapper();
		BookDto bookDto = new BookDto();
		bookDto.setBookshelfId(foreignShelfId);
		bookDto.setName("Тестовая книга для первой полки юзера test1@test.com");
		bookDto.setDescription("Дескриптион");
		this.mockMvc.perform(post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(bookDto))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isForbidden());
	}

	@Test
	public void updateBook() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Book book = bookRepository.findById(8L).get();
		Assert.assertEquals("Первая книга первой полки юзера test1", book.getName());
		Assert.assertEquals("Я заебался фикстуры переделывать", book.getDescription());
		BookDto bookDto = new BookDto();
		bookDto.setDescription("Changed description");
		bookDto.setName("Changed Name");
		this.mockMvc.perform(put("/books/".concat(String.valueOf(book.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(bookDto))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(book.getId().intValue())))
				.andExpect(jsonPath("$.name", is("Changed Name")))
				.andExpect(jsonPath("$.description", is("Changed description")));
		book.setName("Первая книга первой полки юзера test1");
		book.setDescription("Я заебался фикстуры переделывать");
		bookRepository.save(book);
	}

	@Test
	public void deleteBook() throws Exception {
		Book book = new Book(bookshelfRepository.findById(6L).get(), "name", "desc");
		Long bookId = bookRepository.save(book).getId();
		this.mockMvc.perform(delete("/books/8")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());
		Assert.assertEquals(Optional.empty(), bookRepository.findById(8L));
		bookRepository.deleteById(bookId);
	}

	@Test
	public void failDeleteForeignBook() throws Exception {
		BookshelfDto bookshelfDto = new BookshelfDto();
		bookshelfDto.setName("Полка для другого юзера");
		Long foreignShelfId = bookshelfService.saveNewBookshelf(bookshelfDto, 4L).getId();
		Book book = new Book();
		book.setBookshelf(bookshelfRepository.findById(foreignShelfId).get());
		book.setName("Тестовая книга для первой полки юзера test3@test.com");
		book.setDescription("Дескриптион");
		Long foreignBookId = bookRepository.save(book).getId();
		this.mockMvc.perform(delete("/books/".concat(String.valueOf(foreignBookId)))
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isForbidden());
	}

	@Test
	public void addBookText() throws Exception {
		File file = new File("pg1661.txt");
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "multipart/form-data",
				new FileInputStream(file));
		this.mockMvc.perform(fileUpload("/books/8")
				.file(multipartFile)
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());
		// :(
		Thread.sleep(1000);
		pageRepository.deleteAll();
		bookRepository.findById(8L).get().setLastPage(1);
		bookRepository.findById(8L).get().setPagesAmount(0);
	}

	@Test
	public void getBook() throws Exception {
		this.mockMvc.perform(get("/books/8")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(8)))
				.andExpect(jsonPath("$.name", is("Первая книга первой полки юзера test1")))
				.andExpect(jsonPath("$.description", is("Я заебался фикстуры переделывать")));
	}

	@Test
	public void getPage() throws Exception {
		Page page = new Page();
		page.setBook(bookRepository.findById(8L).get());
		page.setNumeration(1);
		page.setText("Test text");
		pageRepository.save(page);
		this.mockMvc.perform(get("/books/8/pages/1")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numeration", is(1)))
				.andExpect(jsonPath("$.text", is("Test text")));
		pageRepository.deleteAll();
	}

	@Test
	public void continueReading() throws Exception {
		Page page = new Page();
		page.setBook(bookRepository.findById(8L).get());
		page.setNumeration(1);
		page.setText("Test text");
		pageRepository.save(page);
		this.mockMvc.perform(get("/books/8/continuereading")
				.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numeration", is(1)))
				.andExpect(jsonPath("$.text", is("Test text")));
		pageRepository.deleteAll();
	}

	@Test
	public void moveBookToAnotherBookshelf() throws Exception {
		this.mockMvc.perform(put("/books/8/movetoanotherbookshelf/7")
		.header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
				.andExpect(status().isOk());
		Assert.assertEquals(7, bookRepository.findById(8L).get().getBookshelf().getId().intValue());
		Book book = bookRepository.findById(8L).get();
		book.setBookshelf(bookshelfRepository.findById(5L).get());
		bookRepository.save(book);
	}
}