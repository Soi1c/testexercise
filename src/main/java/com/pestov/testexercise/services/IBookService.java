package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.dto.PageDto;
import com.pestov.testexercise.models.Bookshelf;

import java.io.File;
import java.util.List;

public interface IBookService {

	BookDto saveNewBook(BookDto bookDto, Bookshelf bookshelf);

	void addTextToBook(File file, Long bookId);

	PageDto continueReading(Long bookId);

	void changeBookshelf(Long bookId, Long bookshelfId);

	boolean isBookBelongToUser(long bookId);

	List<BookDto> allBooksByBookshelf(Long bookshelfId);

	BookDto getBookById(Long bookId);

	PageDto getPageByNum(Long bookId, int pageNum);

	BookDto updateBook(Long bookId, BookDto bookDto);

	void deleteBook(Long bookId);

	PageDto getSharedPageByNum(Long bookId, int pageNum);

	PageDto continueReadingSharedBook(Long bookId);
}
