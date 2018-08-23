package com.pestov.testexercise.controllers;

import com.pestov.testexercise.dto.*;
import com.pestov.testexercise.services.IBookService;
import com.pestov.testexercise.services.IBookshelfService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
	public ResponseEntity<List<UserDto>> getUsersList(@RequestAttribute Long customUserId) {
		return new ResponseEntity<>(userService.getUsers(customUserId), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookshelfDto>> getBookshelvesByUser(@PathVariable Long userId) {
		return new ResponseEntity<>(bookshelfService.bookshelvesByUser(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/bookshelves/{bookshelfId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookDto>> getBooksByBookshelfForUser(@PathVariable Long userId,
																	@PathVariable Long bookshelfId) {
		return new ResponseEntity<>(bookService.allBooksByBookshelf(bookshelfId), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/bookshelves/{bookshelfId}/{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BookDto> getBook(@PathVariable Long userId,
										@PathVariable Long bookshelfId,
										@PathVariable Long bookId) {
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
	}

	@RequestMapping(value = "booksharingrequest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity createBookSharingRequest(@RequestBody BookSharingDto bookSharingDto, @RequestAttribute Long customUserId) {
		if (userService.isRequestAlreadySent(bookSharingDto.getBook_id(), customUserId)) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		userService.createBookSharingRequest(bookSharingDto, customUserId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "getmyrequests", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookSharingDto>> getMyRequests(@RequestAttribute Long customUserId) {
		return new ResponseEntity<>(userService.getMyRequests(customUserId), HttpStatus.OK);
	}

	@RequestMapping(value = "allowrequest/{booksharingId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<BookSharingDto> allowRequestById(@PathVariable Long booksharingId,
														   @RequestParam(required = false, value = "expireDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate expireDate,
														   @RequestAttribute Long customUserId) {
		if (!userService.findBooksharingById(booksharingId).getOwnerUser().getId().equals(customUserId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return new ResponseEntity<>(userService.allowBooksharingRequestById(booksharingId, expireDate), HttpStatus.OK);
	}

	@RequestMapping(value = "refuserequest/{booksharingId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<BookSharingDto> refuseRequestById(@PathVariable Long booksharingId,
															@RequestBody BookSharingDto bookSharingDto,
															@RequestAttribute Long customUserId) {
		if (!userService.findBooksharingById(booksharingId).getOwnerUser().getId().equals(customUserId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return new ResponseEntity<>(userService.refuseBooksharingRequestById(booksharingId, bookSharingDto), HttpStatus.OK);
	}

	@RequestMapping(value = "myrefusedrequests", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookSharingDto>> myRefusedRequests(@RequestAttribute Long customUserId) {
		return new ResponseEntity<>(userService.myRefusedRequests(customUserId), HttpStatus.OK);
	}

	@RequestMapping(value = "mysharedbooks", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<BookSharingDto>> mySharedBooks(@RequestAttribute Long customUserId) {
		return new ResponseEntity<>(userService.mySharedBooks(customUserId), HttpStatus.OK);
	}

	@RequestMapping(value = "mysharedbooks/{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BookDto> getSharedBook(@PathVariable Long bookId) {
		return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
	}

	@RequestMapping(value = "mysharedbooks/{bookId}/pages/{pageNum}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageDto> getSharedPage(@PathVariable Long bookId,
												 @PathVariable int pageNum,
												 @RequestAttribute Long customUserId) {
		if (!userService.checkBookShared(customUserId, bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return new ResponseEntity<>(bookService.getSharedPageByNum(bookId, pageNum, customUserId), HttpStatus.OK);
	}

	@RequestMapping(value = "mysharedbooks/{bookId}/continuereading", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageDto> continueReadSharedBook(@PathVariable Long bookId, @RequestAttribute Long customUserId) {
		if (!userService.checkBookShared(customUserId, bookId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return new ResponseEntity<>(bookService.continueReadingSharedBook(bookId, customUserId), HttpStatus.OK);
	}
}
