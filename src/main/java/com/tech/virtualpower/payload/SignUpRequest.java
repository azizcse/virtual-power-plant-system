package com.tech.virtualpower.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
public class SignUpRequest {
	@NotBlank(message = "Name is required")
	@Size(min = 4, max = 40)
	private String name;
	@NotBlank(message = "Email is required")
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 20)
	private String password;
}
