package com.pestov.testexercise.dto;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class BookSharingDto {

	@Nullable
	private Long id;

	private Long ownerUserId;

	private Long askingUserId;

	@Nullable
	private boolean isAllowed;

	@Nullable
	private LocalDateTime expireDate;

	private Long book_id;

	@Nullable
	private String refuseDescription;

	@Nullable
	public Long getId() {
		return id;
	}

	public Long getOwnerUserId() {
		return ownerUserId;
	}

	public Long getAskingUserId() {
		return askingUserId;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	@Nullable
	public LocalDateTime getExpireDate() {
		return expireDate;
	}

	public Long getBook_id() {
		return book_id;
	}

	@Nullable
	public String getRefuseDescription() {
		return refuseDescription;
	}

	public void setRefuseDescription(@Nullable String refuseDescription) {
		this.refuseDescription = refuseDescription;
	}
}
