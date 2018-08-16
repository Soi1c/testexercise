package com.pestov.testexercise.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class BookshelfDto {

	@Nullable
	private Long id;

	private String name;
}
