package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookshelfRepository;
import com.pestov.testexercise.repositories.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookshelfService implements IBookshelfService {

	private final BookshelfRepository bookshelfRepository;
	private final BookRepository bookRepository;
	private final PageRepository pageRepository;

	public BookshelfService(BookshelfRepository bookshelfRepository, BookRepository bookRepository, PageRepository pageRepository) {
		this.bookshelfRepository = bookshelfRepository;
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
	}

	public Bookshelf saveNewBookshelf(Long userId, BookshelfDto bookshelfDto) {
		Bookshelf bookshelf = new Bookshelf(userId, bookshelfDto.getName());
		return bookshelfRepository.save(bookshelf);
	}

	public List<Bookshelf> bookshelvesByUser(Long userId) {
		return bookshelfRepository.findAllByUserId(userId);
	}

	public void deleteBookshelf(Long id) {
		List<Book> books = bookRepository.findAllByBookshelfId(id);
		for (Book book : books) {
			pageRepository.deleteAllByBookId(book.getId());
		}
		bookRepository.deleteAllByBookshelfId(id);
		bookshelfRepository.deleteById(id);
	}

	public void renameBookshelf(BookshelfDto bookshelfDto) {
		Bookshelf target = bookshelfRepository.getOne(bookshelfDto.getId());
		target.setName(bookshelfDto.getName());
		bookshelfRepository.save(target);
	}

	public Bookshelf getBookshelfById(Long id) {
		return bookshelfRepository.getOne(id);
	}
}
