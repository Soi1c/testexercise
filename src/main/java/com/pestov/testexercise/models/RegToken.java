package com.pestov.testexercise.models;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reg_token")
public class RegToken {

	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	@Id
	@Column(name = "reg_token")
	private String regToken;

	@Column(name = "creation_time")
	private LocalDateTime creationTime;

	public RegToken() {
	}

	public RegToken(User userId) {
		this.user = userId;
	}

	public User getUserId() {
		return user;
	}

	public void setUserId(User userId) {
		this.user = userId;
	}

	public String getRegToken() {
		return regToken;
	}

	public void setRegToken(String regToken) {
		this.regToken = regToken;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}
}
