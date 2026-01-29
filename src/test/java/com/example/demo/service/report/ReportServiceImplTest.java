package com.example.demo.service.report;

import com.example.demo.dto.loan.LoanReportDTO;
import com.example.demo.dto.report.DashboardStatisticsDTO;
import com.example.demo.dto.review.ReviewReportDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.entity.Review;
import com.example.demo.enums.LoanStatus;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReportServiceImpl;
import com.example.demo.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ReportService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReportService Tests")
class ReportServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private BookMapper bookMapper;

  @InjectMocks
  private ReportServiceImpl reportService;

  private Book book;
  private Loan loan;
  private Review review;

  @BeforeEach
  void setUp() {
    book = TestDataBuilder.createBook();
    loan = new Loan();
    loan.setId(1L);
    loan.setStatus(LoanStatus.BORROWED);
    loan.setBorrowDate(LocalDateTime.now());
    review = TestDataBuilder.createReview();
  }

  @Test
  @DisplayName("UC-REPORT-001: shouldGetDashboardStatistics_WhenValid")
  void shouldGetDashboardStatistics_WhenValid() {
    // Arrange
    when(bookRepository.count()).thenReturn(10L);
    when(userRepository.count()).thenReturn(5L);
    when(loanRepository.countByStatus(LoanStatus.BORROWED)).thenReturn(3L);
    when(loanRepository.countByStatus(LoanStatus.OVERDUE)).thenReturn(1L);
    Pageable pageable = PageRequest.of(0, 5);
    List<Object[]> mostBorrowedRaw = new ArrayList<>();
    mostBorrowedRaw.add(new Object[] { book, 5L });
    when(bookRepository.findMostBorrowedBooks(pageable)).thenReturn(mostBorrowedRaw);
    when(bookMapper.toDTO(book)).thenReturn(TestDataBuilder.createBookDTO());

    // Act
    DashboardStatisticsDTO result = reportService.getDashboardStatistics();

    // Assert
    assertNotNull(result);
    assertEquals(10L, result.getTotalBooks());
    assertEquals(5L, result.getTotalUsers());
    assertEquals(3L, result.getActiveLoans());
    assertEquals(1L, result.getOverdueLoans());
    verify(bookRepository, times(1)).count();
    verify(userRepository, times(1)).count();
    verify(loanRepository, times(1)).countByStatus(LoanStatus.BORROWED);
    verify(loanRepository, times(1)).countByStatus(LoanStatus.OVERDUE);
  }

  @Test
  @DisplayName("UC-REPORT-002: shouldGetLoanReport_WhenValidDateRange")
  void shouldGetLoanReport_WhenValidDateRange() {
    // Arrange
    LocalDate startDate = LocalDate.now().minusDays(30);
    LocalDate endDate = LocalDate.now();
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);
    List<Loan> loans = List.of(loan);

    when(loanRepository.findLoansByDateRange(startDateTime, endDateTime)).thenReturn(loans);
    when(loanRepository.countLoansByDateRangeAndStatus(startDateTime, endDateTime, LoanStatus.BORROWED)).thenReturn(5L);
    when(loanRepository.countLoansByDateRangeAndStatus(startDateTime, endDateTime, LoanStatus.RETURNED)).thenReturn(3L);

    // Act
    LoanReportDTO result = reportService.getLoanReport(startDate, endDate);

    // Assert
    assertNotNull(result);
    assertEquals(startDate, result.getStartDate());
    assertEquals(endDate, result.getEndDate());
    verify(loanRepository, times(1)).findLoansByDateRange(startDateTime, endDateTime);
  }

  @Test
  @DisplayName("UC-REPORT-003: shouldGetReviewReport_WhenBookIdProvided")
  void shouldGetReviewReport_WhenBookIdProvided() {
    // Arrange
    Long bookId = 1L;
    List<Review> reviews = List.of(review);
    Double averageRating = 4.5;
    Long totalReviews = 10L;

    when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(book));
    when(reviewRepository.findListByBookId(bookId)).thenReturn(reviews);
    when(reviewRepository.getAverageRatingByBookId(bookId)).thenReturn(averageRating);
    when(reviewRepository.countReviewsByBookId(bookId)).thenReturn(totalReviews);

    // Act
    ReviewReportDTO result = reportService.getReviewReport(bookId);

    // Assert
    assertNotNull(result);
    assertEquals(bookId, result.getBookId());
    assertEquals(averageRating, result.getAverageRating());
    assertEquals(totalReviews, result.getTotalReviews());
    verify(bookRepository, times(1)).findById(bookId);
    verify(reviewRepository, times(1)).findListByBookId(bookId);
  }

  @Test
  @DisplayName("UC-REPORT-003: shouldGetReviewReport_WhenNoBookId")
  void shouldGetReviewReport_WhenNoBookId() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 5);
    List<Book> topRatedBooks = List.of(book);
    Double minRating = 4.0;

    when(bookRepository.findTopRatedBooks(minRating, pageable)).thenReturn(topRatedBooks);
    when(bookMapper.toDTO(book)).thenReturn(TestDataBuilder.createBookDTO());

    // Act
    ReviewReportDTO result = reportService.getReviewReport(null);

    // Assert
    assertNotNull(result);
    verify(bookRepository, times(1)).findTopRatedBooks(minRating, pageable);
  }
}
