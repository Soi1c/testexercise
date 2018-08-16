package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.errors.ApiError;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
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

	@PostMapping
	@ResponseBody
	public ResponseEntity<Book> addNewBook(@RequestBody BookDto bookDto) {
		Bookshelf bookshelf = bookshelfService.getBookshelfById(bookDto.getBookshelfId());
		Book book = bookService.saveNewBook(bookDto, bookshelf);
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}",method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody BookDto bookDto) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Book book = bookService.updateBook(bookId, bookDto);
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

 	@RequestMapping(value = "{bookId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity deleteBook(@PathVariable Long bookId) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		bookService.deleteBook(bookId);
		return ResponseEntity.ok("ok");
	}


	@RequestMapping(value = "{bookId}", method = RequestMethod.POST)
	public ResponseEntity addBookText(@RequestParam("file") MultipartFile file, @PathVariable Long bookId) throws IOException {
		if (!bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!file.getOriginalFilename().endsWith(".txt")) {
			return new ResponseEntity<>(ApiError.WRONG_EXTENSION.toString(), HttpStatus.NOT_ACCEPTABLE);
		}
		File usualFile = new File(bookId.toString());
		FileUtils.writeByteArrayToFile(usualFile, file.getBytes());
		bookService.addTextToBook(usualFile, bookId);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/pages/{pageNum}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Page> getPage(@PathVariable Long bookId, @PathVariable int pageNum) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return new ResponseEntity<>(bookService.getPageByNum(bookId, pageNum), HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/continuereading", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Page> continueReading(@PathVariable Long bookId) {
		if (bookService.getBookById(bookId) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		if (!bookService.isBookBelongToUser(bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Page lastReadPage = bookService.continueReading(bookId);
		return new ResponseEntity<>(lastReadPage, HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/movetoanotherbookshelf/{bookshelfId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity moveBookToAnotherBookshelf(@PathVariable Long bookId, @PathVariable Long bookshelfId) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!bookshelfService.bookshelvesByUser(getLoggedUserId())
				.contains(bookshelfService.getBookshelfById(bookshelfId))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		bookService.changeBookshelf(bookId, bookshelfId);
		return new ResponseEntity(HttpStatus.OK);
	}
}
