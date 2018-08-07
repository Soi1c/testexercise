package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
