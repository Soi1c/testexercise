package com.pestov.testexercise.controllers;

import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.services.IBookService;
import com.pestov.testexercise.services.IBookshelfService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping(value = "{userId}/bookshelves/{bookshelfId}/{bookId}")
	@ResponseBody
	public ResponseEntity<Book> getBook(@PathVariable Long userId,
										@PathVariable Long bookshelfId,
										@PathVariable Long bookId) {
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
	}


}
