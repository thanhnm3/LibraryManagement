package com.example.demo.dto.user;

import com.example.demo.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleDTO {

	@NotNull(message = "Role is required")
	private UserRole role;
}
