package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.PageRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Service
public class BookService implements IBookService {

	private final BookRepository bookRepository;

	private final PageRepository pageRepository;

	private final BookshelfService bookshelfService;

	public BookService(BookRepository bookRepository, PageRepository pageRepository, BookshelfService bookshelfService) {
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
		this.bookshelfService = bookshelfService;
	}

	public Book saveNewBook(BookDto bookDto) {
		Book book = new Book(bookDto);
		bookRepository.save(book);
		return book;
	}

	public String getTextOfPage(Long bookId, int numeration) {
		Page targetPage = pageRepository.findPageByBookIdAndNumeration(bookId, numeration);
		Book book = bookRepository.findById(bookId).get();
		book.setLastPage(numeration);
		bookRepository.save(book);
		return targetPage.getText().toString();
	}

	public int continueReading(Long bookId) {
		return bookRepository.findById(bookId).get().getLastPage();
	}

	public void changeBookshelf(Long bookId, Long bookshelfId) {
		Book book = bookRepository.findById(bookId).get();
		book.setBookshelfId(bookshelfId);
		bookRepository.save(book);
	}

	public boolean isBookBelongToUser(long bookId) {
		Long bookshelfId = bookRepository.findById(bookId).get().getBookshelfId();
		if (!bookshelfService.bookshelvesByUser(getLoggedUserId()).contains(bookshelfId)) {
			return false;
		}
		return true;
	}

	public List<Book> allBooksByBookshelf(Long bookshelfId) {
		return bookRepository.findAllByBookshelfId(bookshelfId);
	}

	@Async
	public void addTextToBook(File file, Long bookId) {
		if (pageRepository.existsByBookId(bookId)) pageRepository.deleteAllByBookId(bookId);
		int pageAmount = 0;
		try {
			pageAmount = divideBookToPages(file, bookId);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		file.delete();
		bookRepository.findById(bookId).get().setPagesAmount(pageAmount);
	}

	private int divideBookToPages(File file, Long bookId) throws IOException, SQLException {
		List<String> lines = org.apache.commons.io.IOUtils.readLines(new FileReader(file));
		int pageNumber = 1;
		while (!lines.isEmpty()) {
			if (lines.size() > 30) {
				String pageText = "";
				for (int i = 0; i < 30; i++) {
					pageText = pageText.concat(lines.get(i)).concat("\n");
				}
				Page page = new Page(bookId, pageNumber++, new javax.sql.rowset.serial.SerialClob(pageText.toCharArray()));
				pageRepository.save(page);
				lines.subList(0, 30).clear();
			} else {
				String pageText = "";
				for (String line : lines) {
					pageText = pageText.concat(line).concat("\n");
				}
				lines.clear();
				Page page = new Page(bookId, pageNumber++, new javax.sql.rowset.serial.SerialClob(pageText.toCharArray()));
				pageRepository.save(page);
				lines.clear();
			}
		}
		return pageNumber;
	}
}
