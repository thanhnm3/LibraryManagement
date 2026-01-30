package com.example.demo.dto.loan;

import com.example.demo.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

	private Long id;
	private Long userId;
	private String userFullName;
	private Long bookId;
	private String bookTitle;
	private LocalDateTime borrowDate;
	private LocalDateTime dueDate;
	private LocalDateTime returnDate;
	private LoanStatus status;
}
