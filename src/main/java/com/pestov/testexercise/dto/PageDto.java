package com.pestov.testexercise.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class PageDto {

	@Nullable
	private Long id;

	@Nullable
	private int numeration;

	@Nullable
	private String text;
}
