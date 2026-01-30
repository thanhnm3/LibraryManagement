package com.example.demo.dto.user;

import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;
	private String email;
	private String fullName;
	private UserStatus status;
	private UserRole role;
	private LocalDateTime createdAt;
}
