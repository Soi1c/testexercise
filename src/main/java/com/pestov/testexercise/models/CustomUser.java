package com.pestov.testexercise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class CustomUser {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(name="is_active")
    private boolean isActive;

    @JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<Bookshelf> bookshelves = new ArrayList<>();

	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="ownerUser")
	private List<BookSharing> ownerBooksharings = new ArrayList<>();

	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="askingUser")
	private List<BookSharing> askingBooksharings = new ArrayList<>();

    public CustomUser() {}

    public CustomUser(String email, String password) {
        this.email = email;
        this.password = password;
        this.isActive = false;
    }
}
