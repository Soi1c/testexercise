package com.pestov.testexercise.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@Setter
public class BookSharingDto {

	@Nullable
	private Long id;

	@Nullable
	private Long ownerUserId;

	@Nullable
	private Long askingUserId;

	@Nullable
	private boolean isAllowed;

	@Nullable
	private LocalDate expireDate;

	@Nullable
	private Long book_id;

	@Nullable
	private String refuseDescription;

	@Nullable
	private String askingUsername;

	@Nullable
	private String bookName;

	@Nullable
	private String bookshelfName;

	@Nullable
	private boolean refused;
}
