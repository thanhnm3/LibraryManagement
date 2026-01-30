package com.example.demo.service.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookDetailDTO;
import com.example.demo.dto.book.BookRequestDTO;
import com.example.demo.dto.book.BookUpdateDTO;
import com.example.demo.dto.review.ReviewSummaryDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.entity.Loan;
import com.example.demo.entity.Publisher;
import com.example.demo.entity.Review;
import com.example.demo.enums.LoanStatus;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.PublisherRepository;
import com.example.demo.repository.ReviewRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for BookServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private PublisherRepository publisherRepository;

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private BookMapper bookMapper;

  @InjectMocks
  private BookServiceImpl bookService;

  private Book testBook;
  private BookDTO testBookDTO;
  private BookDetailDTO testBookDetailDTO;
  private BookRequestDTO testBookRequestDTO;
  private BookUpdateDTO testBookUpdateDTO;
  private Publisher testPublisher;
  private Author testAuthor;
  private Category testCategory;
  private Long testBookId;
  private Long testPublisherId;

  @BeforeEach
  void setUp() {
    testBookId = 1L;
    testPublisherId = 1L;
    testBook = TestDataBuilder.createBook();
    testBookDTO = TestDataBuilder.createBookDTO();
    testBookDetailDTO = TestDataBuilder.createBookDetailDTO();
    testBookRequestDTO = TestDataBuilder.createBookRequestDTO();
    testBookUpdateDTO = TestDataBuilder.createBookUpdateDTO();
    testPublisher = TestDataBuilder.createPublisher();
    testAuthor = TestDataBuilder.createAuthor();
    testCategory = TestDataBuilder.createCategory();
  }

  /**
   * UC-BOOK-001: createBook
   * Test: shouldCreateBook_WhenValidRequest
   */
  @Test
  @DisplayName("Should create book when valid request")
  void shouldCreateBook_WhenValidRequest() {
    // Arrange
    List<Long> authorIds = List.of(1L);
    List<Long> categoryIds = List.of(1L);
    testBookRequestDTO.setPublisherId(testPublisherId);
    testBookRequestDTO.setAuthorIds(authorIds);
    testBookRequestDTO.setCategoryIds(categoryIds);

    when(bookRepository.findByIsbn(testBookRequestDTO.getIsbn())).thenReturn(Optional.empty());
    when(publisherRepository.findById(testPublisherId)).thenReturn(Optional.of(testPublisher));
    when(authorRepository.findAllById(authorIds)).thenReturn(List.of(testAuthor));
    when(categoryRepository.findAllById(categoryIds)).thenReturn(List.of(testCategory));
    when(bookMapper.toEntity(testBookRequestDTO)).thenReturn(testBook);
    when(bookRepository.save(testBook)).thenReturn(testBook);
    when(bookMapper.toDTO(testBook)).thenReturn(testBookDTO);

    // Act
    BookDTO result = bookService.createBook(testBookRequestDTO);

    // Assert
    assertNotNull(result);
    verify(bookRepository, times(1)).findByIsbn(testBookRequestDTO.getIsbn());
    verify(publisherRepository, times(1)).findById(testPublisherId);
    verify(authorRepository, times(1)).findAllById(authorIds);
    verify(categoryRepository, times(1)).findAllById(categoryIds);
    verify(bookRepository, times(1)).save(testBook);
    verify(bookMapper, times(1)).toDTO(testBook);
  }

  /**
   * UC-BOOK-001: createBook
   * Test: shouldThrowException_WhenIsbnExists
   */
  @Test
  @DisplayName("Should throw exception when ISBN exists")
  void shouldThrowException_WhenIsbnExists() {
    // Arrange
    when(bookRepository.findByIsbn(testBookRequestDTO.getIsbn())).thenReturn(Optional.of(testBook));

    // Act & Assert
    assertThrows(DuplicateResourceException.class, () -> {
      bookService.createBook(testBookRequestDTO);
    });

    verify(bookRepository, times(1)).findByIsbn(testBookRequestDTO.getIsbn());
    verify(publisherRepository, never()).findById(any());
    verify(bookRepository, never()).save(any());
  }

  /**
   * UC-BOOK-001: createBook
   * Test: shouldThrowException_WhenPublisherNotFound
   */
  @Test
  @DisplayName("Should throw exception when publisher not found")
  void shouldThrowException_WhenPublisherNotFound() {
    // Arrange
    testBookRequestDTO.setPublisherId(testPublisherId);
    when(bookRepository.findByIsbn(testBookRequestDTO.getIsbn())).thenReturn(Optional.empty());
    when(publisherRepository.findById(testPublisherId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      bookService.createBook(testBookRequestDTO);
    });

    verify(bookRepository, times(1)).findByIsbn(testBookRequestDTO.getIsbn());
    verify(publisherRepository, times(1)).findById(testPublisherId);
    verify(bookRepository, never()).save(any());
  }

  /**
   * UC-BOOK-001: createBook
   * Test: shouldThrowException_WhenAuthorNotFound
   */
  @Test
  @DisplayName("Should throw exception when author not found")
  void shouldThrowException_WhenAuthorNotFound() {
    // Arrange
    List<Long> authorIds = List.of(1L, 2L);
    testBookRequestDTO.setPublisherId(testPublisherId);
    testBookRequestDTO.setAuthorIds(authorIds);

    when(bookRepository.findByIsbn(testBookRequestDTO.getIsbn())).thenReturn(Optional.empty());
    when(publisherRepository.findById(testPublisherId)).thenReturn(Optional.of(testPublisher));
    when(bookMapper.toEntity(testBookRequestDTO)).thenReturn(testBook);
    when(authorRepository.findAllById(authorIds)).thenReturn(List.of(testAuthor));

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      bookService.createBook(testBookRequestDTO);
    });

    verify(authorRepository, times(1)).findAllById(authorIds);
    verify(bookRepository, never()).save(any());
  }

  /**
   * UC-BOOK-001: createBook
   * Test: shouldThrowException_WhenCategoryNotFound
   */
  @Test
  @DisplayName("Should throw exception when category not found")
  void shouldThrowException_WhenCategoryNotFound() {
    // Arrange
    List<Long> categoryIds = List.of(1L, 2L);
    testBookRequestDTO.setPublisherId(testPublisherId);
    testBookRequestDTO.setCategoryIds(categoryIds);

    when(bookRepository.findByIsbn(testBookRequestDTO.getIsbn())).thenReturn(Optional.empty());
    when(publisherRepository.findById(testPublisherId)).thenReturn(Optional.of(testPublisher));
    when(bookMapper.toEntity(testBookRequestDTO)).thenReturn(testBook);
    when(categoryRepository.findAllById(categoryIds)).thenReturn(List.of(testCategory));

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      bookService.createBook(testBookRequestDTO);
    });

    verify(categoryRepository, times(1)).findAllById(categoryIds);
    verify(bookRepository, never()).save(any());
  }

  /**
   * UC-BOOK-002: getAllBooks
   * Test: shouldGetAllBooks_WithPagination
   */
  @Test
  @DisplayName("Should get all books with pagination")
  void shouldGetAllBooks_WithPagination() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books = List.of(testBook);
    Page<Book> bookPage = new PageImpl<>(books, pageable, 1);

    when(bookRepository.findAll(pageable)).thenReturn(bookPage);
    when(bookMapper.toDTO(testBook)).thenReturn(testBookDTO);

    // Act
    Page<BookDTO> result = bookService.getAllBooks(pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(bookRepository, times(1)).findAll(pageable);
    verify(bookMapper, times(1)).toDTO(testBook);
  }

  /**
   * UC-BOOK-003: getBookById
   * Test: shouldGetBookById_WhenExists
   */
  @Test
  @DisplayName("Should get book by id when exists")
  void shouldGetBookById_WhenExists() {
    // Arrange
    List<Review> reviews = List.of(TestDataBuilder.createReview());
    ReviewSummaryDTO reviewSummary = new ReviewSummaryDTO();
    reviewSummary.setAverageRating(5.0);
    reviewSummary.setTotalReviews(1L);

    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(reviewRepository.findListByBookId(testBookId)).thenReturn(reviews);
    when(bookMapper.toDetailDTO(testBook)).thenReturn(testBookDetailDTO);

    // Act
    BookDetailDTO result = bookService.getBookById(testBookId);

    // Assert
    assertNotNull(result);
    verify(bookRepository, times(1)).findById(testBookId);
    verify(reviewRepository, times(1)).findListByBookId(testBookId);
    verify(bookMapper, times(1)).toDetailDTO(testBook);
  }

  /**
   * UC-BOOK-003: getBookById
   * Test: shouldThrowException_WhenBookNotFound
   */
  @Test
  @DisplayName("Should throw exception when book not found")
  void shouldThrowException_WhenBookNotFound() {
    // Arrange
    when(bookRepository.findById(testBookId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      bookService.getBookById(testBookId);
    });

    verify(bookRepository, times(1)).findById(testBookId);
    verify(reviewRepository, never()).findListByBookId(any());
    verify(bookMapper, never()).toDetailDTO(any());
  }

  /**
   * UC-BOOK-004: updateBook
   * Test: shouldUpdateBook_WhenValidRequest
   */
  @Test
  @DisplayName("Should update book when valid request")
  void shouldUpdateBook_WhenValidRequest() {
    // Arrange
    String newIsbn = "978-0-987654-32-1";
    testBookUpdateDTO.setIsbn(newIsbn);

    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(bookRepository.findByIsbn(newIsbn)).thenReturn(Optional.empty());
    when(bookRepository.save(testBook)).thenReturn(testBook);
    when(bookMapper.toDTO(testBook)).thenReturn(testBookDTO);

    // Act
    BookDTO result = bookService.updateBook(testBookId, testBookUpdateDTO);

    // Assert
    assertNotNull(result);
    verify(bookRepository, times(1)).findById(testBookId);
    verify(bookRepository, times(1)).findByIsbn(newIsbn);
    verify(bookMapper, times(1)).updateEntityFromDTO(testBook, testBookUpdateDTO);
    verify(bookRepository, times(1)).save(testBook);
    verify(bookMapper, times(1)).toDTO(testBook);
  }

  /**
   * UC-BOOK-004: updateBook
   * Test: shouldThrowException_WhenNewIsbnExists
   */
  @Test
  @DisplayName("Should throw exception when new ISBN exists")
  void shouldThrowException_WhenNewIsbnExists() {
    // Arrange
    String newIsbn = "978-0-987654-32-1";
    testBookUpdateDTO.setIsbn(newIsbn);
    Book existingBook = TestDataBuilder.createBook(2L, "Another Book", newIsbn);

    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(bookRepository.findByIsbn(newIsbn)).thenReturn(Optional.of(existingBook));

    // Act & Assert
    assertThrows(DuplicateResourceException.class, () -> {
      bookService.updateBook(testBookId, testBookUpdateDTO);
    });

    verify(bookRepository, times(1)).findById(testBookId);
    verify(bookRepository, times(1)).findByIsbn(newIsbn);
    verify(bookRepository, never()).save(any());
  }

  /**
   * UC-BOOK-004: updateBook
   * Test: shouldUpdateBook_WhenPublisherIdChanged
   */
  @Test
  @DisplayName("Should update book when publisher id changed")
  void shouldUpdateBook_WhenPublisherIdChanged() {
    // Arrange
    Long newPublisherId = 2L;
    Publisher newPublisher = TestDataBuilder.createPublisher(newPublisherId, "New Publisher");
    testBookUpdateDTO.setPublisherId(newPublisherId);

    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(publisherRepository.findById(newPublisherId)).thenReturn(Optional.of(newPublisher));
    when(bookRepository.save(testBook)).thenReturn(testBook);
    when(bookMapper.toDTO(testBook)).thenReturn(testBookDTO);

    // Act
    BookDTO result = bookService.updateBook(testBookId, testBookUpdateDTO);

    // Assert
    assertNotNull(result);
    verify(publisherRepository, times(1)).findById(newPublisherId);
    verify(bookRepository, times(1)).save(testBook);
  }

  /**
   * UC-BOOK-005: deleteBook
   * Test: shouldDeleteBook_WhenNoActiveLoans
   */
  @Test
  @DisplayName("Should delete book when no active loans")
  void shouldDeleteBook_WhenNoActiveLoans() {
    // Arrange
    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(loanRepository.findListByBookIdAndStatus(testBookId, LoanStatus.BORROWED))
        .thenReturn(new ArrayList<>());

    // Act
    bookService.deleteBook(testBookId);

    // Assert
    verify(bookRepository, times(1)).findById(testBookId);
    verify(loanRepository, times(1)).findListByBookIdAndStatus(testBookId, LoanStatus.BORROWED);
    verify(bookRepository, times(1)).delete(testBook);
  }

  /**
   * UC-BOOK-005: deleteBook
   * Test: shouldThrowException_WhenHasActiveLoans
   */
  @Test
  @DisplayName("Should throw exception when has active loans")
  void shouldThrowException_WhenHasActiveLoans() {
    // Arrange
    List<Loan> activeLoans = List.of(TestDataBuilder.createLoan());

    when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));
    when(loanRepository.findListByBookIdAndStatus(testBookId, LoanStatus.BORROWED))
        .thenReturn(activeLoans);

    // Act & Assert
    assertThrows(BusinessRuleException.class, () -> {
      bookService.deleteBook(testBookId);
    });

    verify(bookRepository, times(1)).findById(testBookId);
    verify(loanRepository, times(1)).findListByBookIdAndStatus(testBookId, LoanStatus.BORROWED);
    verify(bookRepository, never()).delete(any());
  }
}
