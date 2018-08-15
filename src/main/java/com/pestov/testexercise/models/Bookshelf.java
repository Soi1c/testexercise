package com.pestov.testexercise.models;


import javax.persistence.*;

@Entity
@Table(name = "bookshelves")
public class Bookshelf {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	private String name;

	public Bookshelf(){}

	public Bookshelf(Long userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
