package com.pestov.testexercise.models;

import javax.persistence.*;
import java.time.LocalDate;

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
	private boolean allowed = false;

	@Column(name = "expire_date")
	private LocalDate expireDate;

	@Column(name = "book_id")
	private Long book_id;

	@Column(name = "refuse_description")
	private String refuseDescription;

	@Column(name = "last_page")
	private int lastPage = 1;

	public BookSharing(Long ownerUserId, Long askingUserId, LocalDate expireDate, Long book_id) {
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
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	public Long getBook_id() {
		return book_id;
	}

	public void setBook_id(Long book_id) {
		this.book_id = book_id;
	}

	public String getRefuseDescription() {
		return refuseDescription;
	}

	public void setRefuseDescription(String refuseDescription) {
		this.refuseDescription = refuseDescription;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
}
