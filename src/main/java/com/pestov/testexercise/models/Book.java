package com.pestov.testexercise.models;

import com.pestov.testexercise.dto.BookDto;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "bookshelf_id")
	private Long bookshelfId;

	private String name;

	private String description;

	@Column(name = "last_page")
	private int lastPage = 1;

	@Column(name = "total_amount")
	private int pagesAmount;

	public Book(Long bookshelfId, String name) {
		this.bookshelfId = bookshelfId;
		this.name = name;
	}

	public Book(Long bookshelfId, String name, String description) {
		this.bookshelfId = bookshelfId;
		this.name = name;
		this.description = description;
	}

	public Book(BookDto bookDto) {
		if (bookDto.getDescription() != null) {
			this.bookshelfId = bookDto.getBookshelfId();
			this.name = bookDto.getName();
			this.description = bookDto.getDescription();
		} else {
			this.bookshelfId = bookDto.getBookshelfId();
			this.name = bookDto.getName();
		}
	}

	public Long getId() {
		return id;
	}

	public Long getBookshelfId() {
		return bookshelfId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getPagesAmount() {
		return pagesAmount;
	}

	public void setPagesAmount(int pagesAmount) {
		this.pagesAmount = pagesAmount;
	}
}
