package com.example.demo.service;

import com.example.demo.dto.loan.LoanDTO;
import com.example.demo.dto.loan.LoanDetailDTO;
import com.example.demo.dto.loan.LoanRenewalRequestDTO;
import com.example.demo.dto.loan.LoanRequestDTO;
import com.example.demo.dto.loan.LoanStatisticsDTO;
import com.example.demo.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Loan operations
 */
public interface LoanService {

	/**
	 * UC-LOAN-001: Mượn sách
	 * Tạo loan mới khi user mượn sách
	 * 
	 * @param request - Thông tin mượn sách
	 * @return LoanDTO - Thông tin loan đã tạo
	 */
	LoanDTO borrowBook(LoanRequestDTO request);

	/**
	 * UC-LOAN-002: Trả sách
	 * Cập nhật loan khi user trả sách
	 * 
	 * @param loanId - ID của loan
	 * @return LoanDTO - Thông tin loan đã cập nhật
	 */
	LoanDTO returnBook(Long loanId);

	/**
	 * UC-LOAN-003: Gia hạn mượn sách
	 * Cập nhật due date của loan
	 * 
	 * @param loanId  - ID của loan
	 * @param request - Thông tin gia hạn
	 * @return LoanDTO - Thông tin loan đã cập nhật
	 */
	LoanDTO renewLoan(Long loanId, LoanRenewalRequestDTO request);

	/**
	 * UC-LOAN-004: Lấy danh sách mượn trả
	 * Lấy danh sách loan với pagination và filter
	 * 
	 * @param status   - Filter theo status (optional)
	 * @param userId   - Filter theo userId (optional)
	 * @param bookId   - Filter theo bookId (optional)
	 * @param pageable - Pagination info
	 * @return Page<LoanDTO> - Danh sách loan
	 */
	Page<LoanDTO> getAllLoans(LoanStatus status, Long userId, Long bookId, Pageable pageable);

	/**
	 * UC-LOAN-005: Lấy chi tiết mượn trả
	 * Lấy thông tin chi tiết của loan
	 * 
	 * @param loanId - ID của loan
	 * @return LoanDetailDTO - Thông tin chi tiết loan
	 */
	LoanDetailDTO getLoanById(Long loanId);

	/**
	 * UC-LOAN-006: Lấy lịch sử mượn của user
	 * 
	 * @param userId - ID của user
	 * @return List<LoanDTO> - Danh sách loan của user
	 */
	List<LoanDTO> getLoanHistoryByUserId(Long userId);

	/**
	 * UC-LOAN-007: Lấy sách đang mượn của user
	 * 
	 * @param userId - ID của user
	 * @return List<LoanDTO> - Danh sách loan đang mượn
	 */
	List<LoanDTO> getActiveLoansByUserId(Long userId);

	/**
	 * UC-LOAN-008: Lấy sách quá hạn
	 * 
	 * @param userId - ID của user (optional)
	 * @return List<LoanDTO> - Danh sách loan quá hạn
	 */
	List<LoanDTO> getOverdueLoans(Long userId);

	/**
	 * UC-LOAN-010: Thống kê mượn trả
	 * 
	 * @param startDate - Ngày bắt đầu
	 * @param endDate   - Ngày kết thúc
	 * @return LoanStatisticsDTO - Thống kê mượn trả
	 */
	LoanStatisticsDTO getLoanStatistics(LocalDateTime startDate, LocalDateTime endDate);
}
