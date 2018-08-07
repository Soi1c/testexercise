package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.errors.ApiError;
import com.pestov.testexercise.services.IBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping(value = "books")
public class BookController {

	private final IBookService bookService;

	public BookController(IBookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity addNewBook(@RequestBody BookDto bookDto) {
		bookService.saveNewBook(bookDto);
		return new ResponseEntity("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}", method = RequestMethod.POST)
	public ResponseEntity addBookText(@RequestParam("file") MultipartFile file, @PathVariable Long bookId) throws IOException, SQLException {
		if (!file.getOriginalFilename().endsWith(".txt")) {
			return new ResponseEntity(ApiError.WRONG_EXTENSION.toString(), HttpStatus.NOT_ACCEPTABLE);
		}
		bookService.addTextToBook(file, bookId);
		return new ResponseEntity("ok", HttpStatus.OK);
	}
}
