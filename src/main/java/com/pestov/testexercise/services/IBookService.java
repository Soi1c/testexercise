package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Page;

import java.io.File;
import java.util.List;

public interface IBookService {

	Book saveNewBook(BookDto bookDto);

	void addTextToBook(File file, Long bookId);

	String getTextOfPage(Long bookId, int pageId);

	Page continueReading(Long bookId);

	void changeBookshelf(Long bookId, Long bookshelfId);

	boolean isBookBelongToUser(long bookId);

	List<Book> allBooksByBookshelf(Long bookshelfId);

	Book getBookById(Long bookId);

	Page getPageByNum(Long bookId, int pageNum);

	Book updateBook(Long bookId, BookDto bookDto);

	void deleteBook(Long bookId);

	Page getSharedPageByNum(Long bookId, int pageNum);

	Page continueReadingSharedBook(Long bookId);
}
