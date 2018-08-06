package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {

	//TODO удалять еще и связанные книги
	List<Bookshelf> findAllByUserId(Long userId);
}
