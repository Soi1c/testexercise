package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.BookSharing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookSharingRepository extends JpaRepository<BookSharing, Long> {

	List<BookSharing> findAllByOwnerUserIdAndAllowedIsFalse(Long ownerId);
}
