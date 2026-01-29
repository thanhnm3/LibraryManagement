package com.example.demo.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanReportDTO {

	private LocalDate startDate;
	private LocalDate endDate;
	private Map<LocalDate, Long> borrowsByDate;
	private Map<LocalDate, Long> returnsByDate;
	private Long totalBorrows;
	private Long totalReturns;
}
