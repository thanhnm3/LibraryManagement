package com.example.demo.dto.loan;

import com.example.demo.dto.book.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatisticsDTO {

	private Long totalBorrowed;
	private Long totalReturned;
	private Long totalOverdue;
	private List<BookDTO> mostBorrowedBooks;
}
