package com.pestov.testexercise.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_sharing")
public class BookSharing {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "owner_user_id")
	private Long ownerUserId;

	@Column(name = "asking_user_id")
	private Long askingUserId;

	@Column(name = "is_allowed")
	private boolean isAllowed = false;

	@Column(name = "expire_date")
	private LocalDateTime expireDate;

	@Column(name = "book_id")
	private Long book_id;

	@Column(name = "refuse_description")
	private String refuseDescription;

	public BookSharing(Long ownerUserId, Long askingUserId, LocalDateTime expireDate, Long book_id) {
		this.ownerUserId = ownerUserId;
		this.askingUserId = askingUserId;
		this.expireDate = expireDate;
		this.book_id = book_id;
	}

	public BookSharing(Long ownerUserId, Long askingUserId, Long book_id) {
		this.ownerUserId = ownerUserId;
		this.askingUserId = askingUserId;
		this.book_id = book_id;
	}

	public Long getId() {
		return id;
	}

	public Long getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(Long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public Long getAskingUserId() {
		return askingUserId;
	}

	public void setAskingUserId(Long askingUserId) {
		this.askingUserId = askingUserId;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean allowed) {
		isAllowed = allowed;
	}

	public LocalDateTime getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDateTime expireDate) {
		this.expireDate = expireDate;
	}

	public Long getBook_id() {
		return book_id;
	}

	public void setBook_id(Long book_id) {
		this.book_id = book_id;
	}
}
