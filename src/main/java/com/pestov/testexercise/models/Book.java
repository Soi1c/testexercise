package com.pestov.testexercise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pestov.testexercise.dto.BookDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "books")
@Getter
@Setter
public class Book {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookshelf_id")
	private Bookshelf bookshelf;

	private String name;

	private String description;

	@Column(name = "last_page")
	private int lastPage = 1;

	@Column(name = "total_amount")
	private int pagesAmount;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "book")
	private List<Page> pages = new ArrayList<>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "book")
	private List<BookSharing> bookSharings = new ArrayList<>();

	public Book() {
	}

	public Book(Bookshelf bookshelf, String name, String description) {
		this.bookshelf = bookshelf;
		this.name = name;
		this.description = description;
	}

	public Book(BookDto bookDto, Bookshelf bookshelf) {
		this.bookshelf = bookshelf;
		this.name = bookDto.getName();
		this.description = bookDto.getDescription();
	}
}
