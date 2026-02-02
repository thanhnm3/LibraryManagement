package com.example.demo.dto.auth;

import com.example.demo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

	private String accessToken;
	private AuthUserDTO user;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AuthUserDTO {
		private Long id;
		private String email;
		private String fullName;
		private UserRole role;
	}
}
