package com.pestov.testexercise.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book_sharing")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class BookSharing {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_user")
	private CustomUser ownerUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asking_user")
	private CustomUser askingUser;

	@Column(name = "is_allowed")
	private boolean allowed = false;

	@Column(name = "expire_date")
	private LocalDate expireDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book")
	private Book book;

	@Column(name = "refuse_description")
	private String refuseDescription;

	@Column(name = "last_page")
	private int lastPage = 1;

	public BookSharing() {}

	public BookSharing(CustomUser ownerUser, CustomUser askingUser, LocalDate expireDate, Book book) {
		this.ownerUser = ownerUser;
		this.askingUser = askingUser;
		this.expireDate = expireDate;
		this.book = book;
	}

	public BookSharing(CustomUser ownerUser, CustomUser askingUser, Book book) {
		this.ownerUser = ownerUser;
		this.askingUser = askingUser;
		this.book = book;
	}
}
