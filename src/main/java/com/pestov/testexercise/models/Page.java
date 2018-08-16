package com.pestov.testexercise.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pages")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class Page {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;

	private int numeration;

	@Column(length = 32000)
	private String text;

	public Page() {}

	public Page(Book book, int numeration, String text) {
		this.book = book;
		this.numeration = numeration;
		this.text = text;
	}
}
