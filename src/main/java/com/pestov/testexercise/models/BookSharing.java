package com.pestov.testexercise.models;

import javax.persistence.*;
import java.time.LocalDateTime;

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
	private boolean isAllowed;

	@Column(name = "expire_date")
	private LocalDateTime expireDate;
}
