package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.CustomUser;

import java.time.LocalDate;
import java.util.List;

public interface IUserService {

    CustomUser registerNewUser(String userDto);

	List<UserDto> getUsers();

	BookSharing createBookSharingRequest(BookSharingDto bookSharingDto);

	List<BookSharingDto> getMyRequests();

	BookSharing allowBooksharingRequestById(Long booksharingId, BookSharingDto bookSharingDto);

	BookSharing refuseBooksharingRequestById(Long booksharingId, BookSharingDto bookSharingDto);

	void deleteExpiredBooksharings(LocalDate yesterday);

	List<BookSharing> myRefusedRequests();

	List<BookSharing> mySharedBooks();

	boolean checkBookShared(Long bookId);

	BookSharing findBooksharingByLoggedAskingUserIdAndBookId(Long bookId);
}
