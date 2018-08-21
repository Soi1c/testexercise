package com.pestov.testexercise.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class UserDto {

	@Nullable
    private String email;

	@Nullable
	private String password;

    @Nullable
    private Long id;
}
