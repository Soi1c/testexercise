package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.PageRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class BookService implements IBookService {

	private final BookRepository bookRepository;

	private final PageRepository pageRepository;

	public BookService(BookRepository bookRepository, PageRepository pageRepository) {
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
	}

	public void saveNewBook(BookDto bookDto) {
		Book book = new Book(bookDto);
		bookRepository.save(book);
	}

	public void addTextToBook(MultipartFile file, Long bookId) throws IOException, SQLException {
		File usualFile = new File(new UUID(1, 1).toString());
		FileUtils.writeByteArrayToFile(usualFile, file.getBytes());
		int pageAmount = divideBookToPages(usualFile, bookId);
		usualFile.delete();
		bookRepository.findById(bookId).get().setPagesAmount(pageAmount);
	}

	private int divideBookToPages(File file, Long bookId) throws IOException, SQLException {
		List<String> lines = org.apache.commons.io.IOUtils.readLines(new FileReader(file));
		int pageNumber = 1;
		while (!lines.isEmpty()) {
			if (lines.size() > 30) {
				String pageText = "";
				for (int i = 0; i < 30; i++) {
					pageText.concat(lines.get(i)).concat("\n");
				}
				Page page = new Page(bookId, pageNumber++, new javax.sql.rowset.serial.SerialClob(pageText.toCharArray()));
				pageRepository.save(page);
				for (int i = 0; i < 30; i++) {
					lines.remove(0);
				}
			} else {
				String pageText = "";
				for (int i = 0; i < lines.size(); i++) {
					pageText.concat(lines.get(i));
					lines.remove(i);
				}
				Page page = new Page(bookId, pageNumber++, new javax.sql.rowset.serial.SerialClob(pageText.toCharArray()));
				pageRepository.save(page);
				lines.clear();
			}
		}
		return pageNumber;
	}
}
