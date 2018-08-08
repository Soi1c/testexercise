package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.errors.ApiError;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.services.IBookService;
import com.pestov.testexercise.services.IBookshelfService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Controller
@RequestMapping(value = "books")
public class BookController {

	private final IBookService bookService;

	private final IBookshelfService bookshelfService;

	public BookController(IBookService bookService, IBookshelfService bookshelfService) {
		this.bookService = bookService;
		this.bookshelfService = bookshelfService;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewBook(@RequestBody BookDto bookDto) {
		bookService.saveNewBook(bookDto);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}", method = RequestMethod.POST)
	public ResponseEntity addBookText(@RequestParam("file") MultipartFile file, @PathVariable Long bookId) {
		if (bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!file.getOriginalFilename().endsWith(".txt")) {
			return new ResponseEntity<>(ApiError.WRONG_EXTENSION.toString(), HttpStatus.NOT_ACCEPTABLE);
		}
		File usualFile = new File(bookId.toString());
		try {
			FileUtils.writeByteArrayToFile(usualFile, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		bookService.addTextToBook(usualFile, bookId);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/pages/{pageNum}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getPage(@PathVariable Long bookId, @PathVariable int pageNum) {
		if (bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(bookService.getTextOfPage(bookId, pageNum), HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/continuereading", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> continueReading(@PathVariable Long bookId) {
		if (bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		int lastReadPageNumber = bookService.continueReading(bookId);
		return new ResponseEntity<>(bookService.getTextOfPage(bookId, lastReadPageNumber), HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/movetoanotherbookshelf/{bookshelfId}", method = RequestMethod.POST)
	public ResponseEntity moveBookToAnotherBookshelf(@PathVariable Long bookId, @PathVariable Long bookshelfId) {
		if (bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!bookshelfService.bookshelvesByUser(getLoggedUserId()).contains(bookshelfId)) {
			return new ResponseEntity("Bookshelf does not belong to user!!!", HttpStatus.FORBIDDEN);
		}
		bookService.changeBookshelf(bookId, bookshelfId);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "bookshelf={bookshelfId}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> allBooksByBookshelf(@PathVariable Long bookshelfId) {
		return new ResponseEntity<>(bookService.allBooksByBookshelf(bookshelfId), HttpStatus.OK);
	}
}
