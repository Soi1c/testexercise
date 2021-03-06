package com.pestov.testexercise.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
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
