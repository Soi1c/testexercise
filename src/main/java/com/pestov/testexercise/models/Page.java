package com.pestov.testexercise.models;

import javax.persistence.*;
import java.sql.Clob;

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

	@Column(columnDefinition = "CLOB")
	@Lob
	private java.sql.Clob text;

	public Page(Long bookId, int numeration, Clob text) {
		this.bookId = bookId;
		this.numeration = numeration;
		this.text = text;
	}
}
