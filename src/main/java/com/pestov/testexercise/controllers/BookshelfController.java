package com.pestov.testexercise.controllers;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.dto.BookshelfDto;
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
	@JsonIgnore
	public ResponseEntity<BookshelfDto> submit(@RequestBody BookshelfDto request) {
		if (request.getName() == null || request.getName().equals("")) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		BookshelfDto resultDto = bookshelfService.saveNewBookshelf(request);
		return new ResponseEntity<>(resultDto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookshelfDto>> list() {
		return new ResponseEntity<>(bookshelfService.bookshelvesByUser(getLoggedUserId()), HttpStatus.OK);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookDto>> getBooksByBookshelf(@PathVariable Long id) {
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
