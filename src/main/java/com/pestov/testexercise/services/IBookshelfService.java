package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Bookshelf;

import java.util.List;

public interface IBookshelfService {

	BookshelfDto saveNewBookshelf(BookshelfDto bookshelfDto, Long customUserId);

	List<BookshelfDto> bookshelvesByUser(Long userId);

	List<Bookshelf> bookshelfInstancesByUser(Long userId);

	void deleteBookshelf(Long id);

	void renameBookshelf(BookshelfDto bookshelfDto);

	Bookshelf getBookshelfById(Long id);
}
