package com.example.demo.dto.loan;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {

	@NotNull(message = "User ID is required")
	private Long userId;

	@NotNull(message = "Book ID is required")
	private Long bookId;

	@NotNull(message = "Due date is required")
	@Future(message = "Due date must be in the future")
	private LocalDateTime dueDate;
}
