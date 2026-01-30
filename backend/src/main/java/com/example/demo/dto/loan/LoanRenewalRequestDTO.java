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
public class LoanRenewalRequestDTO {

	@NotNull(message = "New due date is required")
	@Future(message = "New due date must be in the future")
	private LocalDateTime newDueDate;
}
