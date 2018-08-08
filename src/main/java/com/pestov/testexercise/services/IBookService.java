package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.Book;

import java.io.File;
import java.util.List;

public interface IBookService {

	void saveNewBook(BookDto bookDto);

	void addTextToBook(File file, Long bookId);

	String getTextOfPage(Long bookId, int pageId);

	int continueReading(Long bookId);

	void changeBookshelf(Long bookId, Long bookshelfId);

	boolean isBookBelongToUser(long bookId);

	List<Book> allBooksByBookshelf(Long bookshelfId);
}
