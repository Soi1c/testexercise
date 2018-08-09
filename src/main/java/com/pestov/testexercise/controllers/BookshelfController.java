package com.pestov.testexercise.controllers;


import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.services.IBookService;
import com.pestov.testexercise.services.IBookshelfService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Controller
@RequestMapping(value = "bookshelf")
public class BookshelfController {

	private final IBookshelfService bookshelfService;

	private final IBookService bookService;

	public BookshelfController(IBookshelfService bookshelfService, IBookService bookService) {
		this.bookshelfService = bookshelfService;
		this.bookService = bookService;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Bookshelf> submit(@RequestBody BookshelfDto request) {
		Bookshelf newBookshelf = bookshelfService.saveNewBookshelf(getLoggedUserId(), request);
		return new ResponseEntity<>(newBookshelf, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Bookshelf>> list() {
		return new ResponseEntity<>(bookshelfService.bookshelvesByUser(getLoggedUserId()), HttpStatus.OK);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Book>> getBooksByBookshelf(@PathVariable Long id) {
		return new ResponseEntity<>(bookService.allBooksByBookshelf(id), HttpStatus.OK);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteBookshelf(@PathVariable Long id) {
		bookshelfService.deleteBookshelf(id);
		JSONObject response = new JSONObject().put("status", "ok");
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> renameBookshelf(@RequestBody BookshelfDto bookshelfDto) {
		bookshelfService.renameBookshelf(bookshelfDto);
		JSONObject response = new JSONObject().put("status", "ok");
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}

}
