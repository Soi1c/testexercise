package com.pestov.testexercise.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookshelves")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class Bookshelf {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private CustomUser user;

	private String name;

	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="bookshelf")
	private List<Book> books = new ArrayList<>();

	public Bookshelf(){}

	public Bookshelf(CustomUser user, String name) {
		this.user = user;
		this.name = name;
	}
}
