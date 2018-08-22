package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.CustomUser;

import java.time.LocalDate;
import java.util.List;

public interface IUserService {

    CustomUser registerNewUser(UserDto userDto);

	List<UserDto> getUsers();

	void createBookSharingRequest(BookSharingDto bookSharingDto, Long customUserId);

	List<BookSharingDto> getMyRequests(Long customUserId);

	BookSharingDto allowBooksharingRequestById(Long booksharingId, LocalDate expireDate);

	BookSharingDto refuseBooksharingRequestById(Long booksharingId, BookSharingDto bookSharingDto);

	void deleteExpiredBooksharings(LocalDate yesterday);

	List<BookSharingDto> myRefusedRequests(Long customUserId);

	List<BookSharingDto> mySharedBooks(Long customUserId);

	boolean checkBookShared(Long bookId, Long customUserId);

	BookSharing findBooksharingByAskingUserIdAndBookId(Long customUserId, Long bookId);

	Boolean isRequestAlreadySent(Long bookId, Long customUserId);

	Boolean isEmailAlreadyExists(String email);

	BookSharing findBooksharingById(Long booksharingId);
}
