package com.pestov.testexercise.models;

import javax.persistence.*;

@Entity
@Table(name = "pages")
public class Page {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "book_id")
	private Long bookId;

	private int numeration;

	private String text;

	public Page() {
	}

	public Page(Long bookId, int numeration, String text) {
		this.bookId = bookId;
		this.numeration = numeration;
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
