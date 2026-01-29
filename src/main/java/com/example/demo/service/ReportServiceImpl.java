package com.example.demo.service;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.loan.LoanReportDTO;
import com.example.demo.dto.report.DashboardStatisticsDTO;
import com.example.demo.dto.review.ReviewReportDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.entity.Review;
import com.example.demo.enums.LoanStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for Report operations (UC-REPORT-001 ~ UC-REPORT-003)
 */
@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

	private static final int TOP_BOOKS_SIZE = 5;
	private static final double MIN_RATING_TOP_BOOKS = 4.0;

	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final LoanRepository loanRepository;
	private final ReviewRepository reviewRepository;
	private final BookMapper bookMapper;

	public ReportServiceImpl(
			BookRepository bookRepository,
			UserRepository userRepository,
			LoanRepository loanRepository,
			ReviewRepository reviewRepository,
			BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
		this.loanRepository = loanRepository;
		this.reviewRepository = reviewRepository;
		this.bookMapper = bookMapper;
	}

	@Override
	public DashboardStatisticsDTO getDashboardStatistics() {
		Long totalBooks = bookRepository.count();
		Long totalUsers = userRepository.count();
		Long activeLoans = loanRepository.countByStatus(LoanStatus.BORROWED);
		Long overdueLoans = loanRepository.countByStatus(LoanStatus.OVERDUE);

		Pageable pageable = PageRequest.of(0, TOP_BOOKS_SIZE);
		List<Object[]> mostBorrowedRaw = bookRepository.findMostBorrowedBooks(pageable);
		List<BookDTO> mostBorrowedBooks = mostBorrowedRaw.stream()
				.map(row -> (Book) row[0])
				.map(bookMapper::toDTO)
				.collect(Collectors.toList());

		DashboardStatisticsDTO dto = new DashboardStatisticsDTO();
		dto.setTotalBooks(totalBooks);
		dto.setTotalUsers(totalUsers);
		dto.setActiveLoans(activeLoans);
		dto.setOverdueLoans(overdueLoans);
		dto.setMostBorrowedBooks(mostBorrowedBooks);
		dto.setTopAuthors(Collections.emptyList());
		return dto;
	}

	@Override
	public LoanReportDTO getLoanReport(LocalDate startDate, LocalDate endDate) {
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);

		List<Loan> loans = loanRepository.findLoansByDateRange(startDateTime, endDateTime);
		Long totalBorrows = loanRepository.countLoansByDateRangeAndStatus(startDateTime, endDateTime, LoanStatus.BORROWED);
		Long totalReturns = loanRepository.countLoansByDateRangeAndStatus(startDateTime, endDateTime, LoanStatus.RETURNED);

		Map<LocalDate, Long> borrowsByDate = loans.stream()
				.filter(l -> l.getStatus() == LoanStatus.BORROWED || l.getBorrowDate() != null)
				.collect(Collectors.groupingBy(l -> l.getBorrowDate().toLocalDate(), Collectors.counting()));
		Map<LocalDate, Long> returnsByDate = loans.stream()
				.filter(l -> l.getReturnDate() != null)
				.collect(Collectors.groupingBy(l -> l.getReturnDate().toLocalDate(), Collectors.counting()));

		LoanReportDTO dto = new LoanReportDTO();
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		dto.setBorrowsByDate(borrowsByDate);
		dto.setReturnsByDate(returnsByDate);
		dto.setTotalBorrows(totalBorrows != null ? totalBorrows : 0L);
		dto.setTotalReturns(totalReturns != null ? totalReturns : 0L);
		return dto;
	}

	@Override
	public ReviewReportDTO getReviewReport(Long bookId) {
		if (bookId != null) {
			Book book = bookRepository.findById(bookId)
					.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
			List<Review> reviews = reviewRepository.findListByBookId(bookId);
			Double averageRating = reviewRepository.getAverageRatingByBookId(bookId);
			Long totalReviews = reviewRepository.countReviewsByBookId(bookId);

			Map<Integer, Long> ratingDistribution = reviews.stream()
					.collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

			ReviewReportDTO dto = new ReviewReportDTO();
			dto.setBookId(bookId);
			dto.setBookTitle(book.getTitle());
			dto.setRatingDistribution(ratingDistribution);
			dto.setAverageRating(averageRating != null ? averageRating : 0.0);
			dto.setTotalReviews(totalReviews != null ? totalReviews : 0L);
			dto.setTopRatedBooks(Collections.emptyList());
			return dto;
		}

		Pageable pageable = PageRequest.of(0, TOP_BOOKS_SIZE);
		List<Book> topRatedBooks = bookRepository.findTopRatedBooks(MIN_RATING_TOP_BOOKS, pageable);
		List<BookDTO> topRatedDTOs = topRatedBooks.stream().map(bookMapper::toDTO).collect(Collectors.toList());

		ReviewReportDTO dto = new ReviewReportDTO();
		dto.setBookId(null);
		dto.setBookTitle(null);
		dto.setRatingDistribution(Collections.emptyMap());
		dto.setAverageRating(0.0);
		dto.setTotalReviews(0L);
		dto.setTopRatedBooks(topRatedDTOs);
		return dto;
	}
}
