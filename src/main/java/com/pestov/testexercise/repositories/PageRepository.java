package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PageRepository extends JpaRepository<Page, Long> {

	@Transactional
	void deleteAllByBookId(Long bookId);

	boolean existsByBookId(Long bookId);

	Page findPageByBookIdAndNumeration(Long bookId, int numeration);
}
