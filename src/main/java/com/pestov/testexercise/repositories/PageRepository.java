package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
