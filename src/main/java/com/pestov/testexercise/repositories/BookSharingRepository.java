package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.BookSharing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface BookSharingRepository extends JpaRepository<BookSharing, Long> {

	List<BookSharing> findAllByOwnerUserIdAndAllowedIsFalse(Long ownerId);

	List<BookSharing> findAllByAskingUserIdAndRefusedIsTrue (Long askingUserId);

	List<BookSharing> findAllByAskingUserIdAndAllowedIsTrue(Long askingUserId);

	List<BookSharing> findAllByExpireDateEquals(LocalDate yesterday);

	BookSharing findByAskingUserIdAndBookId(Long askingUserId, Long bookId);

	BookSharing findByAskingUserIdAndBookIdAndRefusedIsFalse(Long askingUserId, Long bookId);

	BookSharing findByAskingUserIdAndBookIdAndExpireDateBefore(Long askingUserId, Long bookId, LocalDate date);
}
