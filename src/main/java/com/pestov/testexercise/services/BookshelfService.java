package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookshelfRepository;
import com.pestov.testexercise.repositories.PageRepository;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Service
public class BookshelfService implements IBookshelfService {

	private final BookshelfRepository bookshelfRepository;
	private final BookRepository bookRepository;
	private final PageRepository pageRepository;
	private final UserRepository userRepository;

	public BookshelfService(BookshelfRepository bookshelfRepository, BookRepository bookRepository,
							PageRepository pageRepository, UserRepository userRepository) {
		this.bookshelfRepository = bookshelfRepository;
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
		this.userRepository = userRepository;
	}

	public Bookshelf saveNewBookshelf(BookshelfDto bookshelfDto) {
		Bookshelf bookshelf = new Bookshelf(userRepository.getOne(getLoggedUserId()), bookshelfDto.getName());
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
