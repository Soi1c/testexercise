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
	public ResponseEntity<BookshelfDto> submit(@RequestBody BookshelfDto bookshelfDto, @RequestAttribute Long customUserId) {
		if (bookshelfDto.getName() == null || bookshelfDto.getName().equals("")) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		BookshelfDto resultDto = bookshelfService.saveNewBookshelf(bookshelfDto, customUserId);
		return new ResponseEntity<>(resultDto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookshelfDto>> list(@RequestAttribute Long customUserId) {
		return new ResponseEntity<>(bookshelfService.bookshelvesByUser(customUserId), HttpStatus.OK);
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
	public ResponseEntity renameBookshelf(@RequestBody BookshelfDto bookshelfDto) {
		bookshelfService.renameBookshelf(bookshelfDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
