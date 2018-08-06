package com.pestov.testexercise.dto;


import org.springframework.lang.Nullable;

public class BookshelfDto {

	@Nullable
	private Long id;

	private String name;

	public String getName() {
		return name;
	}

	@Nullable
	public Long getId() {
		return id;
	}
}
