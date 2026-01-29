package com.example.demo.controller;

import com.example.demo.dto.loan.LoanDTO;
import com.example.demo.dto.loan.LoanDetailDTO;
import com.example.demo.dto.loan.LoanRenewalRequestDTO;
import com.example.demo.dto.loan.LoanRequestDTO;
import com.example.demo.dto.loan.LoanStatisticsDTO;
import com.example.demo.enums.LoanStatus;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	/**
	 * UC-LOAN-001: Mượn sách
	 */
	@PostMapping
	public ResponseEntity<LoanDTO> borrowBook(@Valid @RequestBody LoanRequestDTO request) {
		LoanDTO loan = loanService.borrowBook(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(loan);
	}

	/**
	 * UC-LOAN-002: Trả sách
	 */
	@PutMapping("/{loanId}/return")
	public ResponseEntity<LoanDTO> returnBook(@PathVariable Long loanId) {
		LoanDTO loan = loanService.returnBook(loanId);
		return ResponseEntity.ok(loan);
	}

	/**
	 * UC-LOAN-003: Gia hạn mượn sách
	 */
	@PutMapping("/{loanId}/renew")
	public ResponseEntity<LoanDTO> renewLoan(
			@PathVariable Long loanId,
			@Valid @RequestBody LoanRenewalRequestDTO request) {
		LoanDTO loan = loanService.renewLoan(loanId, request);
		return ResponseEntity.ok(loan);
	}

	/**
	 * UC-LOAN-004: Lấy danh sách mượn trả
	 */
	@GetMapping
	public ResponseEntity<Page<LoanDTO>> getAllLoans(
			@RequestParam(required = false) LoanStatus status,
			@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long bookId,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<LoanDTO> loans = loanService.getAllLoans(status, userId, bookId, pageable);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-005: Lấy chi tiết mượn trả
	 */
	@GetMapping("/{loanId}")
	public ResponseEntity<LoanDetailDTO> getLoanById(@PathVariable Long loanId) {
		LoanDetailDTO loan = loanService.getLoanById(loanId);
		return ResponseEntity.ok(loan);
	}

	/**
	 * UC-LOAN-006: Lấy lịch sử mượn của user
	 */
	@GetMapping("/users/{userId}/history")
	public ResponseEntity<List<LoanDTO>> getLoanHistoryByUserId(@PathVariable Long userId) {
		List<LoanDTO> loans = loanService.getLoanHistoryByUserId(userId);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-007: Lấy sách đang mượn của user
	 */
	@GetMapping("/users/{userId}/active")
	public ResponseEntity<List<LoanDTO>> getActiveLoansByUserId(@PathVariable Long userId) {
		List<LoanDTO> loans = loanService.getActiveLoansByUserId(userId);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-008: Lấy sách quá hạn
	 */
	@GetMapping("/overdue")
	public ResponseEntity<List<LoanDTO>> getOverdueLoans(@RequestParam(required = false) Long userId) {
		List<LoanDTO> loans = loanService.getOverdueLoans(userId);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-010: Thống kê mượn trả
	 */
	@GetMapping("/statistics")
	public ResponseEntity<LoanStatisticsDTO> getLoanStatistics(
			@RequestParam(required = false) LocalDateTime startDate,
			@RequestParam(required = false) LocalDateTime endDate) {
		LoanStatisticsDTO statistics = loanService.getLoanStatistics(startDate, endDate);
		return ResponseEntity.ok(statistics);
	}
}
