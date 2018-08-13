package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.repositories.BookshelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookshelfService implements IBookshelfService {

	@Autowired
	private BookshelfRepository bookshelfRepository;


	public Bookshelf saveNewBookshelf(Long userId, BookshelfDto bookshelfDto) {
		Bookshelf bookshelf = new Bookshelf(userId, bookshelfDto.getName());
		return bookshelfRepository.save(bookshelf);
	}

	public List<Bookshelf> bookshelvesByUser(Long userId) {
		return bookshelfRepository.findAllByUserId(userId);
	}

	public void deleteBookshelf(Long id) {
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
