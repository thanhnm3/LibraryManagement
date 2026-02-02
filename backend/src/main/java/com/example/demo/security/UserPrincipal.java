package com.example.demo.security;

import com.example.demo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Principal held in SecurityContext after JWT validation. Used for authorization checks.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal {

	private Long id;
	private String email;
	private UserRole role;
}
