package com.pestov.testexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pestov.testexercise.TestexerciseApplicationTests;
import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookSharingRepository;
import com.pestov.testexercise.repositories.PageRepository;
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
public class UserControllerTest extends TestexerciseApplicationTests{

    @Autowired
    private BookSharingRepository bookSharingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsersList() throws Exception {
        this.mockMvc.perform(get("/users")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void failingGetUsersListIfUnauthorized() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection());
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
        bookSharingDto.setBook_id(8L);
        this.mockMvc.perform(post("/users/booksharingrequest")
                .content(mapper.writeValueAsString(bookSharingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk());
    }

    @Test
    public void getMyRequests() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
        this.mockMvc.perform(get("/users/getmyrequests")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].bookName", hasToString(bookRepository.findById(8L).get().getName())));
        bookSharingRepository.deleteById(bsId);
    }

    @Test
    public void allowRequestById() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
        this.mockMvc.perform(put("/users/allowrequest/".concat(String.valueOf(bsId)))
                .param("expireDate", "2020-01-01")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
                .andExpect(status().isOk());
        LocalDate targetDate = LocalDate.of(2020, 1, 1);
        Assert.assertEquals(targetDate, bookSharingRepository.findById(bsId).get().getExpireDate());
        Assert.assertTrue(bookSharingRepository.findById(bsId).get().isAllowed());
		bookSharingRepository.deleteById(bsId);
    }

    @Test
    public void refuseRequestById() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
        BookSharingDto bookSharingDto = new BookSharingDto();
        bookSharingDto.setRefuseDescription("plz no");
        this.mockMvc.perform(put("/users/refuserequest/".concat(String.valueOf(bsId)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookSharingDto))
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest1))
                .andExpect(status().isOk());
        Assert.assertEquals("plz no", bookSharingRepository.findById(bsId).get().getRefuseDescription());
        Assert.assertFalse(bookSharingRepository.findById(bsId).get().isAllowed());
		bookSharingRepository.deleteById(bsId);
    }

    @Test
    public void myRefusedRequests() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
        BookSharing bs =
				bookSharingRepository.findById(bsId).get();
        bs.setRefused(true);
        bookSharingRepository.save(bs);
        Thread.sleep(30000);
        this.mockMvc.perform(get("/users/myrefusedrequests")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
		bookSharingRepository.deleteById(bsId);
    }

    @Test
    public void mySharedBooks() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
		BookSharing bs =
				bookSharingRepository.findById(bsId).get();
		bs.setAllowed(true);
		bookSharingRepository.save(bs);
        this.mockMvc.perform(get("/users/mysharedbooks")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
		bookSharingRepository.deleteById(bsId);
    }

    @Test
    public void getSharedBook() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
		BookSharing bs =
				bookSharingRepository.findById(bsId).get();
		bs.setAllowed(true);
		bookSharingRepository.save(bs);
        this.mockMvc.perform(get("/users/mysharedbooks/8")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", hasToString(bookRepository.findById(8L).get().getName())));
		bookSharingRepository.deleteById(bsId);
    }

    @Test
    public void getSharedPage() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
        BookSharing bs = bookSharingRepository.findById(bsId).get();
        bs.setAllowed(true);
        bookSharingRepository.save(bs);
        Page page = new Page();
        page.setBook(bookRepository.findById(8L).get());
        page.setNumeration(1);
        page.setText("Test text of page");
        pageRepository.save(page);
        this.mockMvc.perform(get("/users/mysharedbooks/8/pages/1")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeration", hasToString("1")))
                .andExpect(jsonPath("$.text", hasToString("Test text of page")));
		bookSharingRepository.deleteById(bsId);
		pageRepository.deleteAll();
    }

    @Test
    public void continueReadSharedBook() throws Exception {
		Long bsId = bookSharingRepository.save(new
				BookSharing(userRepository.findById(2L).get(),
				userRepository.findById(3L).get(),
				bookRepository.findById(8L).get())).getId();
		BookSharing bs = bookSharingRepository.findById(bsId).get();
		bs.setAllowed(true);
		bookSharingRepository.save(bs);
        Page page = new Page();
        page.setBook(bookRepository.findById(8L).get());
        page.setNumeration(1);
        page.setText("Test text of page");
        pageRepository.save(page);
        Page page2 = new Page();
        page2.setBook(bookRepository.findById(8L).get());
        page2.setNumeration(2);
        page2.setText("Test text of page 2");
        pageRepository.save(page2);
        this.mockMvc.perform(get("/users/mysharedbooks/8/continuereading")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeration", hasToString("1")))
                .andExpect(jsonPath("$.text", hasToString("Test text of page")));
        this.mockMvc.perform(get("/users/mysharedbooks/8/pages/2")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeration", hasToString("2")))
                .andExpect(jsonPath("$.text", hasToString("Test text of page 2")));
        this.mockMvc.perform(get("/users/mysharedbooks/8/continuereading")
                .header(HttpHeaders.AUTHORIZATION, authTokenForUserTest2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeration", hasToString("2")))
                .andExpect(jsonPath("$.text", hasToString("Test text of page 2")));
		bookSharingRepository.deleteById(bsId);
		pageRepository.deleteAll();
    }
}