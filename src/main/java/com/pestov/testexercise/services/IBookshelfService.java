package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Bookshelf;

import java.util.List;

public interface IBookshelfService {

	Bookshelf saveNewBookshelf(Long userId, BookshelfDto bookshelfDto);

	List<Bookshelf> bookshelvesByUser(Long userId);

	void deleteBookshelf(Long id);

	void renameBookshelf(BookshelfDto bookshelfDto);
}
