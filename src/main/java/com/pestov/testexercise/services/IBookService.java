package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface IBookService {

	void saveNewBook(BookDto bookDto);

	void addTextToBook(MultipartFile file, Long bookId) throws IOException, SQLException;
}
