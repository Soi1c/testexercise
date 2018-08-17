package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.dto.PageDto;
import com.pestov.testexercise.errors.ApiError;
import com.pestov.testexercise.models.Bookshelf;
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
import java.util.Objects;

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
	public ResponseEntity<BookDto> addNewBook(@RequestBody BookDto bookDto) {
		Bookshelf bookshelf = bookshelfService.getBookshelfById(bookDto.getBookshelfId());
		BookDto resultDto = bookService.saveNewBook(bookDto, bookshelf);
		return new ResponseEntity<>(resultDto, HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}",method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<BookDto> updateBook(@PathVariable Long bookId, @RequestBody BookDto bookDto) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		BookDto resultDto = bookService.updateBook(bookId, bookDto);
		return new ResponseEntity<>(resultDto, HttpStatus.OK);
	}

 	@RequestMapping(value = "{bookId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity deleteBook(@PathVariable Long bookId) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		bookService.deleteBook(bookId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}


	@RequestMapping(value = "{bookId}", method = RequestMethod.POST)
	public ResponseEntity addBookText(@RequestParam("file") MultipartFile file, @PathVariable Long bookId) throws IOException {
		if (!bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".txt")) {
			return new ResponseEntity<>(ApiError.WRONG_EXTENSION.toString(), HttpStatus.NOT_ACCEPTABLE);
		}
		File usualFile = new File(bookId.toString());
		FileUtils.writeByteArrayToFile(usualFile, file.getBytes());
		bookService.addTextToBook(usualFile, bookId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BookDto> getBook(@PathVariable Long bookId) {
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/pages/{pageNum}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageDto> getPage(@PathVariable Long bookId, @PathVariable int pageNum) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return new ResponseEntity<>(bookService.getPageByNum(bookId, pageNum), HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/continuereading", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageDto> continueReading(@PathVariable Long bookId) {
		if (bookService.getBookById(bookId) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		if (!bookService.isBookBelongToUser(bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		PageDto lastReadPage = bookService.continueReading(bookId);
		return new ResponseEntity<>(lastReadPage, HttpStatus.OK);
	}

	@RequestMapping(value = "{bookId}/movetoanotherbookshelf/{bookshelfId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity moveBookToAnotherBookshelf(@PathVariable Long bookId, @PathVariable Long bookshelfId) {
		if (!bookService.isBookBelongToUser(bookId)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!bookshelfService.bookshelfInstancesByUser(getLoggedUserId())
				.contains(bookshelfService.getBookshelfById(bookshelfId))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		bookService.changeBookshelf(bookId, bookshelfId);
		return new ResponseEntity(HttpStatus.OK);
	}
}
