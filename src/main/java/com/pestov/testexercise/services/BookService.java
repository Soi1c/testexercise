package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookSharingRepository;
import com.pestov.testexercise.repositories.PageRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Service
public class BookService implements IBookService {

	private final BookRepository bookRepository;

	private final PageRepository pageRepository;

	private final BookSharingRepository bookSharingRepository;

	private final IBookshelfService bookshelfService;

	private final IUserService userService;

	public BookService(BookRepository bookRepository, PageRepository pageRepository,
					   BookSharingRepository bookSharingRepository, IBookshelfService bookshelfService, IUserService userService) {
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
		this.bookSharingRepository = bookSharingRepository;
		this.bookshelfService = bookshelfService;
		this.userService = userService;
	}

	public Book saveNewBook(BookDto bookDto, Bookshelf bookshelf) {
		Book book = new Book(bookDto, bookshelf);
		bookRepository.save(book);
		return book;
	}

	public Book updateBook(Long bookId, BookDto bookDto) {
		Book book = bookRepository.getOne(bookId);
		book.setName(bookDto.getName());
		book.setDescription(bookDto.getDescription());
		bookRepository.save(book);
		return book;
	}

	public void deleteBook(Long bookId) {
		bookRepository.deleteById(bookId);
	}

	public Book getBookById(Long bookId) {
		return bookRepository.getOne(bookId);
	}

	public String getTextOfPage(Long bookId, int numeration) {
		Page targetPage = pageRepository.findPageByBookIdAndNumeration(bookId, numeration);
		Book book = bookRepository.getOne(bookId);
		book.setLastPage(numeration);
		bookRepository.save(book);
		return targetPage.getText();
	}

	public Page getPageByNum(Long bookId, int pageNum) {
		Book book = bookRepository.getOne(bookId);
		Page page = pageRepository.findPageByBookIdAndNumeration(bookId, pageNum);
		book.setLastPage(pageNum);
		bookRepository.save(book);
		return page;
	}

	public Page getSharedPageByNum(Long bookId, int pageNum) {
		BookSharing bookSharing = userService.findBooksharingByLoggedAskingUserIdAndBookId(bookId);
		Page page = pageRepository.findPageByBookIdAndNumeration(bookId, pageNum);
		bookSharing.setLastPage(pageNum);
		bookSharingRepository.save(bookSharing);
		return page;
	}

	public Page continueReading(Long bookId) {
		return pageRepository.findPageByBookIdAndNumeration(bookId, bookRepository.getOne(bookId).getLastPage());
	}

	public Page continueReadingSharedBook(Long bookId) {
		return pageRepository.findPageByBookIdAndNumeration(bookId,
				userService.findBooksharingByLoggedAskingUserIdAndBookId(bookId).getLastPage());
	}

	public void changeBookshelf(Long bookId, Long bookshelfId) {
		Book book = bookRepository.getOne(bookId);
		book.setBookshelf(bookshelfService.getBookshelfById(bookshelfId));
		bookRepository.save(book);
	}

	public boolean isBookBelongToUser(long bookId) {
		Bookshelf bookshelf = bookRepository.getOne(bookId).getBookshelf();
		return bookshelfService.bookshelvesByUser(getLoggedUserId())
				.contains(bookshelf);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		file.delete();
		Book book = bookRepository.getOne(bookId);
		book.setPagesAmount(pageAmount);
		bookRepository.save(book);
	}

	private int divideBookToPages(File file, Long bookId) throws IOException {
		List<String> lines = org.apache.commons.io.IOUtils.readLines(new FileReader(file));
		int pageNumber = 1;
		Book book = bookRepository.getOne(bookId);
		while (!lines.isEmpty()) {
			if (lines.size() > 30) {
				String pageText = "";
				for (int i = 0; i < 30; i++) {
					pageText = pageText.concat(lines.get(i)).concat("\n");
				}
				Page page = new Page(book, pageNumber++, pageText);
				pageRepository.save(page);
				lines.subList(0, 30).clear();
			} else {
				String pageText = "";
				for (String line : lines) {
					pageText = pageText.concat(line).concat("\n");
				}
				lines.clear();
				Page page = new Page(book, pageNumber++, pageText);
				pageRepository.save(page);
				lines.clear();
			}
		}
		return pageNumber;
	}
}
