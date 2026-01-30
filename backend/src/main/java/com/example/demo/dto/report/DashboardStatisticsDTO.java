package com.example.demo.dto.report;

import com.example.demo.dto.author.AuthorDTO;
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
public class DashboardStatisticsDTO {

	private Long totalBooks;
	private Long totalUsers;
	private Long activeLoans;
	private Long overdueLoans;
	private List<BookDTO> mostBorrowedBooks;
	private List<AuthorDTO> topAuthors;
}
