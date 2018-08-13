package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.services.IBookService;
import com.pestov.testexercise.services.IBookshelfService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "users")
public class UserController {

	private final IUserService userService;
 	private final IBookshelfService bookshelfService;
 	private final IBookService bookService;

	public UserController(IUserService userService, IBookshelfService bookshelfService, IBookService bookService) {
		this.userService = userService;
		this.bookshelfService = bookshelfService;
		this.bookService = bookService;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<CustomUser>> getUsersList() {
		return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Bookshelf>> getBookshelvesByUser(@PathVariable Long userId) {
		return new ResponseEntity<>(bookshelfService.bookshelvesByUser(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/bookshelves/{bookshelfId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Book>> getBooksByBookshelfForUser(@PathVariable Long userId,
																 @PathVariable Long bookshelfId) {
		return new ResponseEntity<>(bookService.allBooksByBookshelf(bookshelfId), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/bookshelves/{bookshelfId}/{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Book> getBook(@PathVariable Long userId,
										@PathVariable Long bookshelfId,
										@PathVariable Long bookId) {
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/bookshelves/{bookshelfId}/{bookId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BookSharing> createBookSharingRequest(@PathVariable Long userId,
																@PathVariable Long bookshelfId,
																@PathVariable Long bookId,
																@RequestBody BookSharingDto bookSharingDto) {
		BookSharing bookSharing = userService.createBookSharingRequest(bookSharingDto);
		return new ResponseEntity<>(bookSharing, HttpStatus.OK);
	}

	@RequestMapping(value = "getMyRequests", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookSharing>> getMyRequests() {
		return new ResponseEntity<>(userService.getMyRequests(), HttpStatus.OK);
	}

	@RequestMapping(value = "allowRequest/{booksharingId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<BookSharing> allowRequestById(@PathVariable Long booksharingId, BookSharingDto bookSharingDto) {
		return new ResponseEntity<>(userService.allowBooksharingRequestById(booksharingId, bookSharingDto), HttpStatus.OK);
	}

	@RequestMapping(value = "refuseRequest/{booksharingId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<BookSharing> refuseRequestById(@PathVariable Long booksharingId, BookSharingDto bookSharingDto) {
		return new ResponseEntity<>(userService.refuseBooksharingRequestById(booksharingId, bookSharingDto), HttpStatus.OK);
	}
}
