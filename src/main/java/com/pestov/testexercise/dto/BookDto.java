package com.pestov.testexercise.dto;

import org.springframework.lang.Nullable;

public class BookDto {

	@Nullable
	private Long id;

	@Nullable
	private String name;

	@Nullable
	private String description;

	@Nullable
	private Long bookshelfId;

	@Nullable
	public Long getId() {
		return id;
	}

	@Nullable
	public String getName() {
		return name;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	@Nullable
	public Long getBookshelfId() {
		return bookshelfId;
	}
}
