package com.pestov.testexercise.dto;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class BookDto {

	@Nullable
	private Long id;

	@Nullable
	private String name;

	@Nullable
	private String description;

	@Nullable
	private long bookshelfId;
}
