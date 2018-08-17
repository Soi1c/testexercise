package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.mapper.Mappers;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookshelfRepository;
import com.pestov.testexercise.repositories.PageRepository;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Service
public class BookshelfService implements IBookshelfService {

	private final BookshelfRepository bookshelfRepository;
	private final BookRepository bookRepository;
	private final PageRepository pageRepository;
	private final UserRepository userRepository;
	private final Mappers mappers;

	public BookshelfService(BookshelfRepository bookshelfRepository,
							BookRepository bookRepository,
							PageRepository pageRepository,
							UserRepository userRepository,
							Mappers mappers) {
		this.bookshelfRepository = bookshelfRepository;
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
		this.userRepository = userRepository;
		this.mappers = mappers;
	}

	public BookshelfDto saveNewBookshelf(BookshelfDto bookshelfDto) {
		Bookshelf bookshelf = new Bookshelf(userRepository.getOne(getLoggedUserId()), bookshelfDto.getName());
		bookshelfRepository.save(bookshelf);
		mappers.getBookshelfMapper().map(bookshelf, bookshelfDto);
		return bookshelfDto;
	}

	public List<BookshelfDto> bookshelvesByUser(Long userId) {
		List<Bookshelf> bookshelves = bookshelfRepository.findAllByUserId(userId);
		List<BookshelfDto> targetDto = new ArrayList<>();
		for (Bookshelf bookshelf: bookshelves) {
			targetDto.add(mappers.getBookshelfMapper().map(bookshelf, new BookshelfDto()));
		}
		return targetDto;
	}

	public List<Bookshelf> bookshelfInstancesByUser(Long userId) {
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
