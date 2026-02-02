package com.example.demo.controller;

import com.example.demo.dto.loan.LoanDTO;
import com.example.demo.dto.loan.LoanDetailDTO;
import com.example.demo.dto.loan.LoanRenewalRequestDTO;
import com.example.demo.dto.loan.LoanRequestDTO;
import com.example.demo.dto.loan.LoanStatisticsDTO;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.UserRole;
import com.example.demo.service.LoanService;
import com.example.demo.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
	 * UC-LOAN-001: Mượn sách (chỉ cho chính mình hoặc ADMIN)
	 */
	@PostMapping
	public ResponseEntity<LoanDTO> borrowBook(
			@Valid @RequestBody LoanRequestDTO request,
			Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		if (!principal.getId().equals(request.getUserId()) && principal.getRole() != UserRole.ADMIN) {
			throw new AccessDeniedException("Cannot create loan for another user");
		}
		LoanDTO loan = loanService.borrowBook(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(loan);
	}

	/**
	 * UC-LOAN-002: Trả sách (chỉ chủ loan hoặc ADMIN)
	 */
	@PutMapping("/{loanId}/return")
	public ResponseEntity<LoanDTO> returnBook(
			@PathVariable Long loanId,
			Authentication authentication) {
		LoanDetailDTO loan = loanService.getLoanById(loanId);
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		Long loanUserId = loan.getUser() != null ? loan.getUser().getId() : null;
		if (loanUserId == null || (!principal.getId().equals(loanUserId) && principal.getRole() != UserRole.ADMIN)) {
			throw new AccessDeniedException("Cannot return this loan");
		}
		LoanDTO result = loanService.returnBook(loanId);
		return ResponseEntity.ok(result);
	}

	/**
	 * UC-LOAN-003: Gia hạn mượn sách (chỉ chủ loan hoặc ADMIN)
	 */
	@PutMapping("/{loanId}/renew")
	public ResponseEntity<LoanDTO> renewLoan(
			@PathVariable Long loanId,
			@Valid @RequestBody LoanRenewalRequestDTO request,
			Authentication authentication) {
		LoanDetailDTO loan = loanService.getLoanById(loanId);
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		Long loanUserId = loan.getUser() != null ? loan.getUser().getId() : null;
		if (loanUserId == null || (!principal.getId().equals(loanUserId) && principal.getRole() != UserRole.ADMIN)) {
			throw new AccessDeniedException("Cannot renew this loan");
		}
		LoanDTO result = loanService.renewLoan(loanId, request);
		return ResponseEntity.ok(result);
	}

	/**
	 * UC-LOAN-004: Lấy danh sách mượn trả (ADMIN only)
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<LoanDTO>> getAllLoans(
			@RequestParam(required = false) LoanStatus status,
			@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long bookId,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<LoanDTO> loans = loanService.getAllLoans(status, userId, bookId, pageable);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-008: Lấy sách quá hạn (ADMIN only)
	 */
	@GetMapping("/overdue")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<LoanDTO>> getOverdueLoans(@RequestParam(required = false) Long userId) {
		List<LoanDTO> loans = loanService.getOverdueLoans(userId);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-010: Thống kê mượn trả (ADMIN only)
	 */
	@GetMapping("/statistics")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<LoanStatisticsDTO> getLoanStatistics(
			@RequestParam(required = false) LocalDateTime startDate,
			@RequestParam(required = false) LocalDateTime endDate) {
		LoanStatisticsDTO statistics = loanService.getLoanStatistics(startDate, endDate);
		return ResponseEntity.ok(statistics);
	}

	/**
	 * UC-LOAN-005: Lấy chi tiết mượn trả (chỉ chủ loan hoặc ADMIN)
	 */
	@GetMapping("/{loanId}")
	public ResponseEntity<LoanDetailDTO> getLoanById(
			@PathVariable Long loanId,
			Authentication authentication) {
		LoanDetailDTO loan = loanService.getLoanById(loanId);
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		Long loanUserId = loan.getUser() != null ? loan.getUser().getId() : null;
		if (loanUserId == null || (!principal.getId().equals(loanUserId) && principal.getRole() != UserRole.ADMIN)) {
			throw new AccessDeniedException("Cannot view this loan");
		}
		return ResponseEntity.ok(loan);
	}

	/**
	 * UC-LOAN-006: Lấy lịch sử mượn của user (chỉ chính mình hoặc ADMIN)
	 */
	@GetMapping("/users/{userId}/history")
	public ResponseEntity<List<LoanDTO>> getLoanHistoryByUserId(
			@PathVariable Long userId,
			Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		if (!principal.getId().equals(userId) && principal.getRole() != UserRole.ADMIN) {
			throw new AccessDeniedException("Cannot view another user's loan history");
		}
		List<LoanDTO> loans = loanService.getLoanHistoryByUserId(userId);
		return ResponseEntity.ok(loans);
	}

	/**
	 * UC-LOAN-007: Lấy sách đang mượn của user (chỉ chính mình hoặc ADMIN)
	 */
	@GetMapping("/users/{userId}/active")
	public ResponseEntity<List<LoanDTO>> getActiveLoansByUserId(
			@PathVariable Long userId,
			Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		if (!principal.getId().equals(userId) && principal.getRole() != UserRole.ADMIN) {
			throw new AccessDeniedException("Cannot view another user's active loans");
		}
		List<LoanDTO> loans = loanService.getActiveLoansByUserId(userId);
		return ResponseEntity.ok(loans);
	}
}
