package com.example.demo.service;

import com.example.demo.dto.loan.LoanDTO;
import com.example.demo.dto.loan.LoanDetailDTO;
import com.example.demo.dto.loan.LoanRenewalRequestDTO;
import com.example.demo.dto.loan.LoanRequestDTO;
import com.example.demo.dto.loan.LoanStatisticsDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.entity.User;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.LoanMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Loan operations
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

	private final LoanRepository loanRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final LoanMapper loanMapper;

	public LoanServiceImpl(
			LoanRepository loanRepository,
			UserRepository userRepository,
			BookRepository bookRepository,
			LoanMapper loanMapper) {
		this.loanRepository = loanRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.loanMapper = loanMapper;
	}

	/**
	 * UC-LOAN-001: Mượn sách
	 * Tạo loan mới khi user mượn sách
	 * 
	 * @param request - Thông tin mượn sách
	 * @return LoanDTO - Thông tin loan đã tạo
	 */
	@Override
	public LoanDTO borrowBook(LoanRequestDTO request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

		if (user.getStatus() != UserStatus.ACTIVE) {
			throw new BusinessException("User must be ACTIVE to borrow books");
		}

		Book book = bookRepository.findById(request.getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

		Long activeLoanCount = loanRepository.countActiveLoansByBookId(book.getId(), LoanStatus.BORROWED);
		if (activeLoanCount > 0) {
			throw new BusinessException("Book is currently borrowed by another user");
		}

		Loan loan = loanMapper.toEntity(request);
		loan.setUser(user);
		loan.setBook(book);
		loan.setBorrowDate(LocalDateTime.now());
		loan.setStatus(LoanStatus.BORROWED);

		Loan savedLoan = loanRepository.save(loan);
		return loanMapper.toDTO(savedLoan);
	}

	/**
	 * UC-LOAN-002: Trả sách
	 * Cập nhật loan khi user trả sách
	 * 
	 * @param loanId - ID của loan
	 * @return LoanDTO - Thông tin loan đã cập nhật
	 */
	@Override
	public LoanDTO returnBook(Long loanId) {
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

		if (loan.getStatus() == LoanStatus.RETURNED) {
			throw new BusinessException("Book has already been returned");
		}

		LocalDateTime now = LocalDateTime.now();
		loan.setReturnDate(now);

		if (loan.getDueDate().isBefore(now)) {
			loan.setStatus(LoanStatus.OVERDUE);
		} else {
			loan.setStatus(LoanStatus.RETURNED);
		}

		Loan updatedLoan = loanRepository.save(loan);
		return loanMapper.toDTO(updatedLoan);
	}

	/**
	 * UC-LOAN-003: Gia hạn mượn sách
	 * Cập nhật due date của loan
	 * 
	 * @param loanId  - ID của loan
	 * @param request - Thông tin gia hạn
	 * @return LoanDTO - Thông tin loan đã cập nhật
	 */
	@Override
	public LoanDTO renewLoan(Long loanId, LoanRenewalRequestDTO request) {
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

		if (loan.getStatus() != LoanStatus.BORROWED) {
			throw new BusinessException("Only BORROWED loans can be renewed");
		}

		if (request.getNewDueDate().isBefore(loan.getDueDate()) || request.getNewDueDate().isEqual(loan.getDueDate())) {
			throw new BusinessException("New due date must be after current due date");
		}

		loan.setDueDate(request.getNewDueDate());
		Loan updatedLoan = loanRepository.save(loan);
		return loanMapper.toDTO(updatedLoan);
	}

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
	@Override
	@Transactional(readOnly = true)
	public Page<LoanDTO> getAllLoans(LoanStatus status, Long userId, Long bookId, Pageable pageable) {
		Page<Loan> loans;

		if (status != null && userId != null) {
			loans = loanRepository.findByUserIdAndStatus(userId, status, pageable);
		} else if (status != null) {
			loans = loanRepository.findByStatus(status, pageable);
		} else if (userId != null) {
			loans = loanRepository.findByUserId(userId, pageable);
		} else if (bookId != null) {
			loans = loanRepository.findByBookId(bookId, pageable);
		} else {
			loans = loanRepository.findAll(pageable);
		}

		return loans.map(loanMapper::toDTO);
	}

	/**
	 * UC-LOAN-005: Lấy chi tiết mượn trả
	 * Lấy thông tin chi tiết của loan
	 * 
	 * @param loanId - ID của loan
	 * @return LoanDetailDTO - Thông tin chi tiết loan
	 */
	@Override
	@Transactional(readOnly = true)
	public LoanDetailDTO getLoanById(Long loanId) {
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

		return loanMapper.toDetailDTO(loan);
	}

	/**
	 * UC-LOAN-006: Lấy lịch sử mượn của user
	 * 
	 * @param userId - ID của user
	 * @return List<LoanDTO> - Danh sách loan của user
	 */
	@Override
	@Transactional(readOnly = true)
	public List<LoanDTO> getLoanHistoryByUserId(Long userId) {
		List<Loan> loans = loanRepository.findListByUserId(userId);
		return loans.stream()
				.map(loanMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * UC-LOAN-007: Lấy sách đang mượn của user
	 * 
	 * @param userId - ID của user
	 * @return List<LoanDTO> - Danh sách loan đang mượn
	 */
	@Override
	@Transactional(readOnly = true)
	public List<LoanDTO> getActiveLoansByUserId(Long userId) {
		List<Loan> loans = loanRepository.findListByUserId(userId);
		return loans.stream()
				.filter(loan -> loan.getStatus() == LoanStatus.BORROWED)
				.map(loanMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * UC-LOAN-008: Lấy sách quá hạn
	 * 
	 * @param userId - ID của user (optional)
	 * @return List<LoanDTO> - Danh sách loan quá hạn
	 */
	@Override
	@Transactional(readOnly = true)
	public List<LoanDTO> getOverdueLoans(Long userId) {
		LocalDateTime now = LocalDateTime.now();
		List<Loan> loans;

		if (userId != null) {
			loans = loanRepository.findOverdueLoansByUserId(userId, now, LoanStatus.BORROWED);
		} else {
			loans = loanRepository.findOverdueLoans(now, LoanStatus.BORROWED);
		}

		return loans.stream()
				.map(loanMapper::toDTO)
				.collect(Collectors.toList());
	}

	/**
	 * UC-LOAN-010: Thống kê mượn trả
	 * 
	 * @param startDate - Ngày bắt đầu
	 * @param endDate   - Ngày kết thúc
	 * @return LoanStatisticsDTO - Thống kê mượn trả
	 */
	@Override
	@Transactional(readOnly = true)
	public LoanStatisticsDTO getLoanStatistics(LocalDateTime startDate, LocalDateTime endDate) {
		List<Loan> loans = loanRepository.findLoansByDateRange(startDate, endDate);
		Long totalBorrowed = (long) loans.size();
		Long totalReturned = loanRepository.countLoansByDateRangeAndStatus(
				startDate, endDate, LoanStatus.RETURNED);
		Long totalOverdue = loanRepository.countLoansByDateRangeAndStatus(
				startDate, endDate, LoanStatus.OVERDUE);

		LoanStatisticsDTO statistics = new LoanStatisticsDTO();
		statistics.setTotalBorrowed(totalBorrowed);
		statistics.setTotalReturned(totalReturned);
		statistics.setTotalOverdue(totalOverdue);

		return statistics;
	}
}
