package com.pestov.testexercise.models;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reg_token")
public class RegToken {

	@OneToOne(targetEntity = CustomUser.class)
	@JoinColumn(name = "user_id")
	private CustomUser customUser;

	@Id
	@Column(name = "reg_token")
	private String regToken;

	@Column(name = "creation_time")
	private LocalDateTime creationTime;

	public RegToken() {
	}

	public RegToken(CustomUser customUserId) {
		this.customUser = customUserId;
	}

	public CustomUser getUserId() {
		return customUser;
	}

	public void setUserId(CustomUser customUserId) {
		this.customUser = customUserId;
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
