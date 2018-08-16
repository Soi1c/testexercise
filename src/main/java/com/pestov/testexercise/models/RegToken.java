package com.pestov.testexercise.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reg_token")
@Getter
@Setter
public class RegToken {

	@OneToOne(targetEntity = CustomUser.class)
	@JoinColumn(name = "user_id")
	private CustomUser customUser;

	@Id
	@Column(name = "reg_token")
	private String regToken;

	@Column(name = "creation_time")
	private LocalDateTime creationTime;

	public RegToken() {}

	public RegToken(CustomUser customUserId) {
		this.customUser = customUserId;
	}
}
