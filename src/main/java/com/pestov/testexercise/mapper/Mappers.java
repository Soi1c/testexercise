package com.pestov.testexercise.mapper;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Mappers {

	private final BookMapper bookMapper;
	private final PageMapper pageMapper;
	private final BookshelfMapper bookshelfMapper;
	private final BooksharingMapper booksharingMapper;

	public Mappers(BookMapper bookMapper,
				   PageMapper pageMapper,
				   BookshelfMapper bookshelfMapper,
				   BooksharingMapper booksharingMapper) {
		this.bookMapper = bookMapper;
		this.pageMapper = pageMapper;
		this.bookshelfMapper = bookshelfMapper;
		this.booksharingMapper = booksharingMapper;
	}
}
