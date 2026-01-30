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
import com.example.demo.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for LoanServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoanService Tests")
class LoanServiceImplTest {

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private LoanMapper loanMapper;

  @InjectMocks
  private LoanServiceImpl loanService;

  private Loan testLoan;
  private LoanDTO testLoanDTO;
  private LoanDetailDTO testLoanDetailDTO;
  private LoanRequestDTO testLoanRequestDTO;
  private LoanRenewalRequestDTO testLoanRenewalRequestDTO;
  private User testUser;
  private Book testBook;
  private Long testLoanId;
  private Long testUserId;
  private Long testBookId;

  @BeforeEach
  void setUp() {
    testLoanId = 1L;
    testUserId = 1L;
    testBookId = 1L;
    testUser = TestDataBuilder.createUser(testUserId, "test@example.com", UserStatus.ACTIVE);
    testBook = TestDataBuilder.createBook();
    testLoan = TestDataBuilder.createLoan(testLoanId, LoanStatus.BORROWED);
    testLoan.setUser(testUser);
    testLoan.setBook(testBook);
    testLoanDTO = TestDataBuilder.createLoanDTO();
    testLoanDetailDTO = TestDataBuilder.createLoanDetailDTO();
    testLoanRequestDTO = TestDataBuilder.createLoanRequestDTO();
    testLoanRenewalRequestDTO = TestDataBuilder.createLoanRenewalRequestDTO();
  }

  /**
   * UC-LOAN-001: borrowBook
   * Test: shouldBorrowBook_WhenValidRequest
   */
  @Test
  @DisplayName("Should borrow book when valid request")
  void shouldBorrowBook_WhenValidRequest() {
    // Arrange
    Loan newLoan = TestDataBuilder.createLoan();
    newLoan.setUser(testUser);
    newLoan.setBook(testBook);

    when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(loanRepository.countActiveLoansByBookId(testBookId, LoanStatus.BORROWED)).thenReturn(0L);
    when(loanMapper.toEntity(testLoanRequestDTO)).thenReturn(newLoan);
    when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    LoanDTO result = loanService.borrowBook(testLoanRequestDTO);

    // Assert
    assertNotNull(result);
    verify(userRepository, times(1)).findById(testUserId);
    verify(bookRepository, times(1)).findById(testBookId);
    verify(loanRepository, times(1)).countActiveLoansByBookId(testBookId, LoanStatus.BORROWED);
    verify(loanRepository, times(1)).save(any(Loan.class));
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-001: borrowBook
   * Test: shouldThrowException_WhenUserNotActive
   */
  @Test
  @DisplayName("Should throw exception when user not active")
  void shouldThrowException_WhenUserNotActive() {
    // Arrange
    User bannedUser = TestDataBuilder.createUser(testUserId, "banned@example.com", UserStatus.BANNED);
    when(userRepository.findById(testUserId)).thenReturn(Optional.of(bannedUser));

    // Act & Assert
    assertThrows(BusinessException.class, () -> {
      loanService.borrowBook(testLoanRequestDTO);
    });

    verify(userRepository, times(1)).findById(testUserId);
    verify(bookRepository, never()).findById(any());
    verify(loanRepository, never()).save(any());
  }

  /**
   * UC-LOAN-001: borrowBook
   * Test: shouldThrowException_WhenBookAlreadyBorrowed
   */
  @Test
  @DisplayName("Should throw exception when book already borrowed")
  void shouldThrowException_WhenBookAlreadyBorrowed() {
    // Arrange
    when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(loanRepository.countActiveLoansByBookId(testBookId, LoanStatus.BORROWED)).thenReturn(1L);

    // Act & Assert
    assertThrows(BusinessException.class, () -> {
      loanService.borrowBook(testLoanRequestDTO);
    });

    verify(loanRepository, times(1)).countActiveLoansByBookId(testBookId, LoanStatus.BORROWED);
    verify(loanRepository, never()).save(any());
  }

  /**
   * UC-LOAN-002: returnBook
   * Test: shouldReturnBook_WhenOnTime
   */
  @Test
  @DisplayName("Should return book when on time")
  void shouldReturnBook_WhenOnTime() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    testLoan.setDueDate(now.plusDays(1));
    testLoan.setStatus(LoanStatus.BORROWED);

    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));
    when(loanRepository.save(testLoan)).thenReturn(testLoan);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    LoanDTO result = loanService.returnBook(testLoanId);

    // Assert
    assertNotNull(result);
    assertEquals(LoanStatus.RETURNED, testLoan.getStatus());
    assertNotNull(testLoan.getReturnDate());
    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanRepository, times(1)).save(testLoan);
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-002: returnBook
   * Test: shouldReturnBook_WhenOverdue
   */
  @Test
  @DisplayName("Should return book when overdue")
  void shouldReturnBook_WhenOverdue() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    testLoan.setDueDate(now.minusDays(1));
    testLoan.setStatus(LoanStatus.BORROWED);

    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));
    when(loanRepository.save(testLoan)).thenReturn(testLoan);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    LoanDTO result = loanService.returnBook(testLoanId);

    // Assert
    assertNotNull(result);
    assertEquals(LoanStatus.OVERDUE, testLoan.getStatus());
    assertNotNull(testLoan.getReturnDate());
    verify(loanRepository, times(1)).save(testLoan);
  }

  /**
   * UC-LOAN-002: returnBook
   * Test: shouldThrowException_WhenAlreadyReturned
   */
  @Test
  @DisplayName("Should throw exception when already returned")
  void shouldThrowException_WhenAlreadyReturned() {
    // Arrange
    testLoan.setStatus(LoanStatus.RETURNED);
    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));

    // Act & Assert
    assertThrows(BusinessException.class, () -> {
      loanService.returnBook(testLoanId);
    });

    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanRepository, never()).save(any());
  }

  /**
   * UC-LOAN-003: renewLoan
   * Test: shouldRenewLoan_WhenValidRequest
   */
  @Test
  @DisplayName("Should renew loan when valid request")
  void shouldRenewLoan_WhenValidRequest() {
    // Arrange
    LocalDateTime currentDueDate = LocalDateTime.now().plusDays(7);
    LocalDateTime newDueDate = LocalDateTime.now().plusDays(14);
    testLoan.setDueDate(currentDueDate);
    testLoan.setStatus(LoanStatus.BORROWED);
    testLoanRenewalRequestDTO.setNewDueDate(newDueDate);

    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));
    when(loanRepository.save(testLoan)).thenReturn(testLoan);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    LoanDTO result = loanService.renewLoan(testLoanId, testLoanRenewalRequestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(newDueDate, testLoan.getDueDate());
    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanRepository, times(1)).save(testLoan);
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-003: renewLoan
   * Test: shouldThrowException_WhenNotBorrowed
   */
  @Test
  @DisplayName("Should throw exception when not borrowed")
  void shouldThrowException_WhenNotBorrowed() {
    // Arrange
    testLoan.setStatus(LoanStatus.RETURNED);
    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));

    // Act & Assert
    assertThrows(BusinessException.class, () -> {
      loanService.renewLoan(testLoanId, testLoanRenewalRequestDTO);
    });

    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanRepository, never()).save(any());
  }

  /**
   * UC-LOAN-003: renewLoan
   * Test: shouldThrowException_WhenNewDueDateBeforeCurrent
   */
  @Test
  @DisplayName("Should throw exception when new due date before current")
  void shouldThrowException_WhenNewDueDateBeforeCurrent() {
    // Arrange
    LocalDateTime currentDueDate = LocalDateTime.now().plusDays(7);
    LocalDateTime newDueDate = LocalDateTime.now().plusDays(5);
    testLoan.setDueDate(currentDueDate);
    testLoan.setStatus(LoanStatus.BORROWED);
    testLoanRenewalRequestDTO.setNewDueDate(newDueDate);

    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));

    // Act & Assert
    assertThrows(BusinessException.class, () -> {
      loanService.renewLoan(testLoanId, testLoanRenewalRequestDTO);
    });

    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanRepository, never()).save(any());
  }

  /**
   * UC-LOAN-004: getAllLoans
   * Test: shouldGetAllLoans_WithFilters
   */
  @Test
  @DisplayName("Should get all loans with filters")
  void shouldGetAllLoans_WithFilters() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Loan> loans = List.of(testLoan);
    Page<Loan> loanPage = new PageImpl<>(loans, pageable, 1);

    when(loanRepository.findByUserIdAndStatus(testUserId, LoanStatus.BORROWED, pageable))
        .thenReturn(loanPage);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    Page<LoanDTO> result = loanService.getAllLoans(LoanStatus.BORROWED, testUserId, null, pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(loanRepository, times(1)).findByUserIdAndStatus(testUserId, LoanStatus.BORROWED, pageable);
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-004: getAllLoans
   * Test: shouldGetAllLoans_WithStatusOnly
   */
  @Test
  @DisplayName("Should get all loans with status only")
  void shouldGetAllLoans_WithStatusOnly() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Loan> loans = List.of(testLoan);
    Page<Loan> loanPage = new PageImpl<>(loans, pageable, 1);

    when(loanRepository.findByStatus(LoanStatus.BORROWED, pageable)).thenReturn(loanPage);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    Page<LoanDTO> result = loanService.getAllLoans(LoanStatus.BORROWED, null, null, pageable);

    // Assert
    assertNotNull(result);
    verify(loanRepository, times(1)).findByStatus(LoanStatus.BORROWED, pageable);
  }

  /**
   * UC-LOAN-004: getAllLoans
   * Test: shouldGetAllLoans_WithUserIdOnly
   */
  @Test
  @DisplayName("Should get all loans with user id only")
  void shouldGetAllLoans_WithUserIdOnly() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Loan> loans = List.of(testLoan);
    Page<Loan> loanPage = new PageImpl<>(loans, pageable, 1);

    when(loanRepository.findByUserId(testUserId, pageable)).thenReturn(loanPage);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    Page<LoanDTO> result = loanService.getAllLoans(null, testUserId, null, pageable);

    // Assert
    assertNotNull(result);
    verify(loanRepository, times(1)).findByUserId(testUserId, pageable);
  }

  /**
   * UC-LOAN-004: getAllLoans
   * Test: shouldGetAllLoans_WithBookIdOnly
   */
  @Test
  @DisplayName("Should get all loans with book id only")
  void shouldGetAllLoans_WithBookIdOnly() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Loan> loans = List.of(testLoan);
    Page<Loan> loanPage = new PageImpl<>(loans, pageable, 1);

    when(loanRepository.findByBookId(testBookId, pageable)).thenReturn(loanPage);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    Page<LoanDTO> result = loanService.getAllLoans(null, null, testBookId, pageable);

    // Assert
    assertNotNull(result);
    verify(loanRepository, times(1)).findByBookId(testBookId, pageable);
  }

  /**
   * UC-LOAN-004: getAllLoans
   * Test: shouldGetAllLoans_WithNoFilters
   */
  @Test
  @DisplayName("Should get all loans with no filters")
  void shouldGetAllLoans_WithNoFilters() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Loan> loans = List.of(testLoan);
    Page<Loan> loanPage = new PageImpl<>(loans, pageable, 1);

    when(loanRepository.findAll(pageable)).thenReturn(loanPage);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    Page<LoanDTO> result = loanService.getAllLoans(null, null, null, pageable);

    // Assert
    assertNotNull(result);
    verify(loanRepository, times(1)).findAll(pageable);
  }

  /**
   * UC-LOAN-005: getLoanById
   * Test: shouldGetLoanById_WhenExists
   */
  @Test
  @DisplayName("Should get loan by id when exists")
  void shouldGetLoanById_WhenExists() {
    // Arrange
    when(loanRepository.findById(testLoanId)).thenReturn(Optional.of(testLoan));
    when(loanMapper.toDetailDTO(testLoan)).thenReturn(testLoanDetailDTO);

    // Act
    LoanDetailDTO result = loanService.getLoanById(testLoanId);

    // Assert
    assertNotNull(result);
    assertEquals(testLoanDetailDTO.getId(), result.getId());
    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanMapper, times(1)).toDetailDTO(testLoan);
  }

  /**
   * UC-LOAN-005: getLoanById
   * Test: shouldThrowException_WhenLoanNotFound
   */
  @Test
  @DisplayName("Should throw exception when loan not found")
  void shouldThrowException_WhenLoanNotFound() {
    // Arrange
    when(loanRepository.findById(testLoanId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      loanService.getLoanById(testLoanId);
    });

    verify(loanRepository, times(1)).findById(testLoanId);
    verify(loanMapper, never()).toDetailDTO(any());
  }

  /**
   * UC-LOAN-006: getLoanHistoryByUserId
   * Test: shouldGetLoanHistory_WhenUserExists
   */
  @Test
  @DisplayName("Should get loan history when user exists")
  void shouldGetLoanHistory_WhenUserExists() {
    // Arrange
    List<Loan> loans = List.of(testLoan);
    when(loanRepository.findListByUserId(testUserId)).thenReturn(loans);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    List<LoanDTO> result = loanService.getLoanHistoryByUserId(testUserId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(loanRepository, times(1)).findListByUserId(testUserId);
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-007: getActiveLoansByUserId
   * Test: shouldGetActiveLoans_WhenUserExists
   */
  @Test
  @DisplayName("Should get active loans when user exists")
  void shouldGetActiveLoans_WhenUserExists() {
    // Arrange
    Loan borrowedLoan = TestDataBuilder.createLoan(testLoanId, LoanStatus.BORROWED);
    Loan returnedLoan = TestDataBuilder.createLoan(2L, LoanStatus.RETURNED);
    List<Loan> loans = List.of(borrowedLoan, returnedLoan);

    when(loanRepository.findListByUserId(testUserId)).thenReturn(loans);
    when(loanMapper.toDTO(borrowedLoan)).thenReturn(testLoanDTO);

    // Act
    List<LoanDTO> result = loanService.getActiveLoansByUserId(testUserId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(loanRepository, times(1)).findListByUserId(testUserId);
    verify(loanMapper, times(1)).toDTO(borrowedLoan);
  }

  /**
   * UC-LOAN-008: getOverdueLoans
   * Test: shouldGetOverdueLoans_WhenUserIdProvided
   */
  @Test
  @DisplayName("Should get overdue loans when user id provided")
  void shouldGetOverdueLoans_WhenUserIdProvided() {
    // Arrange
    List<Loan> loans = List.of(testLoan);

    when(loanRepository.findOverdueLoansByUserId(eq(testUserId), any(LocalDateTime.class), eq(LoanStatus.BORROWED)))
        .thenReturn(loans);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    List<LoanDTO> result = loanService.getOverdueLoans(testUserId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(loanRepository, times(1)).findOverdueLoansByUserId(eq(testUserId), any(LocalDateTime.class), eq(LoanStatus.BORROWED));
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-008: getOverdueLoans
   * Test: shouldGetOverdueLoans_WhenNoUserId
   */
  @Test
  @DisplayName("Should get overdue loans when no user id")
  void shouldGetOverdueLoans_WhenNoUserId() {
    // Arrange
    List<Loan> loans = List.of(testLoan);

    when(loanRepository.findOverdueLoans(any(LocalDateTime.class), eq(LoanStatus.BORROWED)))
        .thenReturn(loans);
    when(loanMapper.toDTO(testLoan)).thenReturn(testLoanDTO);

    // Act
    List<LoanDTO> result = loanService.getOverdueLoans(null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(loanRepository, times(1)).findOverdueLoans(any(LocalDateTime.class), eq(LoanStatus.BORROWED));
    verify(loanMapper, times(1)).toDTO(testLoan);
  }

  /**
   * UC-LOAN-010: getLoanStatistics
   * Test: shouldGetLoanStatistics_WhenValidDateRange
   */
  @Test
  @DisplayName("Should get loan statistics when valid date range")
  void shouldGetLoanStatistics_WhenValidDateRange() {
    // Arrange
    LocalDateTime startDate = LocalDateTime.now().minusDays(30);
    LocalDateTime endDate = LocalDateTime.now();
    List<Loan> loans = List.of(testLoan);

    when(loanRepository.findLoansByDateRange(startDate, endDate)).thenReturn(loans);
    when(loanRepository.countLoansByDateRangeAndStatus(startDate, endDate, LoanStatus.RETURNED))
        .thenReturn(5L);
    when(loanRepository.countLoansByDateRangeAndStatus(startDate, endDate, LoanStatus.OVERDUE))
        .thenReturn(2L);

    // Act
    LoanStatisticsDTO result = loanService.getLoanStatistics(startDate, endDate);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getTotalBorrowed());
    assertEquals(5L, result.getTotalReturned());
    assertEquals(2L, result.getTotalOverdue());
    verify(loanRepository, times(1)).findLoansByDateRange(startDate, endDate);
    verify(loanRepository, times(1)).countLoansByDateRangeAndStatus(startDate, endDate, LoanStatus.RETURNED);
    verify(loanRepository, times(1)).countLoansByDateRangeAndStatus(startDate, endDate, LoanStatus.OVERDUE);
  }
}
