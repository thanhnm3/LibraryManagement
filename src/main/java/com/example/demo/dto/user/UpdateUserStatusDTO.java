package com.example.demo.dto.user;

import com.example.demo.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatusDTO {

	@NotNull(message = "Status is required")
	private UserStatus status;
}
