package com.example.demo.dto.loan;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.user.UserDTO;
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
public class LoanDetailDTO {

	private Long id;
	private UserDTO user;
	private BookDTO book;
	private LocalDateTime borrowDate;
	private LocalDateTime dueDate;
	private LocalDateTime returnDate;
	private LoanStatus status;
}
