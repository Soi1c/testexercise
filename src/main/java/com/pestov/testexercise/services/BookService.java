package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.dto.PageDto;
import com.pestov.testexercise.mapper.Mappers;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.models.Page;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookSharingRepository;
import com.pestov.testexercise.repositories.PageRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Service
public class BookService implements IBookService {

	private final BookRepository bookRepository;
	private final PageRepository pageRepository;
	private final BookSharingRepository bookSharingRepository;
	private final IBookshelfService bookshelfService;
	private final IUserService userService;
	private final Mappers mappers;

	public BookService(BookRepository bookRepository,
					   PageRepository pageRepository,
					   BookSharingRepository bookSharingRepository,
					   IBookshelfService bookshelfService,
					   IUserService userService,
					   Mappers mappers) {
		this.bookRepository = bookRepository;
		this.pageRepository = pageRepository;
		this.bookSharingRepository = bookSharingRepository;
		this.bookshelfService = bookshelfService;
		this.userService = userService;
		this.mappers = mappers;
	}

	public BookDto saveNewBook(BookDto bookDto, Bookshelf bookshelf) {
		Book book = new Book(bookDto, bookshelf);
		bookRepository.save(book);
		bookDto = mappers.getBookMapper().map(book, bookDto);
		return bookDto;
	}

	public BookDto updateBook(Long bookId, BookDto bookDto) {
		Book book = bookRepository.getOne(bookId);
		book.setName(bookDto.getName());
		book.setDescription(bookDto.getDescription());
		bookRepository.save(book);
		mappers.getBookMapper().map(book, bookDto);
		return bookDto;
	}

	public void deleteBook(Long bookId) {
		bookRepository.deleteById(bookId);
	}

	public BookDto getBookById(Long bookId) {
		Book book =  bookRepository.getOne(bookId);
		BookDto bookDto = new BookDto();
		mappers.getBookMapper().map(book, bookDto);
		return bookDto;
	}

	public PageDto getPageByNum(Long bookId, int pageNum) {
		Book book = bookRepository.getOne(bookId);
		Page page = pageRepository.findPageByBookIdAndNumeration(bookId, pageNum);
		book.setLastPage(pageNum);
		bookRepository.save(book);
		PageDto pageDto = new PageDto();
		mappers.getPageMapper().map(page, pageDto);
		return pageDto;
	}

	public PageDto getSharedPageByNum(Long bookId, int pageNum) {
		BookSharing bookSharing = userService.findBooksharingByLoggedAskingUserIdAndBookId(bookId);
		Page page = pageRepository.findPageByBookIdAndNumeration(bookId, pageNum);
		bookSharing.setLastPage(pageNum);
		bookSharingRepository.save(bookSharing);
		PageDto pageDto = new PageDto();
		mappers.getPageMapper().map(page, pageDto);
		return pageDto;
	}

	public PageDto continueReading(Long bookId) {
		Page page =  pageRepository.findPageByBookIdAndNumeration(bookId, bookRepository.getOne(bookId).getLastPage());
		PageDto pageDto = new PageDto();
		mappers.getPageMapper().map(page, pageDto);
		return pageDto;
	}

	public PageDto continueReadingSharedBook(Long bookId) {
		Page page = pageRepository.findPageByBookIdAndNumeration(bookId,
				userService.findBooksharingByLoggedAskingUserIdAndBookId(bookId).getLastPage());
		PageDto pageDto = new PageDto();
		mappers.getPageMapper().map(page, pageDto);
		return pageDto;
	}

	public void changeBookshelf(Long bookId, Long bookshelfId) {
		Book book = bookRepository.getOne(bookId);
		book.setBookshelf(bookshelfService.getBookshelfById(bookshelfId));
		bookRepository.save(book);
	}

	public boolean isBookBelongToUser(long bookId) {
		Bookshelf bookshelf = bookRepository.getOne(bookId).getBookshelf();
		return bookshelfService.bookshelfInstancesByUser(getLoggedUserId())
				.contains(bookshelf);
	}

	public List<BookDto> allBooksByBookshelf(Long bookshelfId) {
		List<Book> books = bookRepository.findAllByBookshelfId(bookshelfId);
		List<BookDto> targetDto = new ArrayList<>();
		for (Book book: books) {
			targetDto.add(mappers.getBookMapper().map(book, new BookDto()));
		}
		return targetDto;
	}

	@Async
	@Transactional
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
