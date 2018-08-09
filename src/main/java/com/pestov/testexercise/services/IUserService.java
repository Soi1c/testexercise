package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.CustomUser;

import java.util.List;

public interface IUserService {

    CustomUser registerNewUser(String userDto);

	List<CustomUser> getUsers();

	BookSharing createBookSharingRequest(BookSharingDto bookSharingDto);

	List<BookSharing> getMyRequests();
}
