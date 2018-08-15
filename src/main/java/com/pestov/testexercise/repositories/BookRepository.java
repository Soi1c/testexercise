package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findAllByBookshelfId(Long bookshelfId);

	void deleteAllByBookshelfId(Long bookshelfId);
}
