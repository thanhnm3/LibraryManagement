package com.example.demo.service.search;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookSearchCriteriaDTO;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.SearchServiceImpl;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for SearchService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchService Tests")
class SearchServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookMapper bookMapper;

  @InjectMocks
  private SearchServiceImpl searchService;

  private Book book;
  private BookDTO bookDTO;

  @BeforeEach
  void setUp() {
    book = TestDataBuilder.createBook();
    bookDTO = TestDataBuilder.createBookDTO();
  }

  @Test
  @DisplayName("UC-SEARCH-001: shouldAdvancedSearch_WhenValidParameters")
  void shouldAdvancedSearch_WhenValidParameters() {
    // Arrange
    String categoryName = "Fiction";
    String authorName = "John";
    Long userId = 1L;
    String title = "Test";

    List<Book> books = List.of(book);
    when(bookRepository.advancedSearch(categoryName, authorName, userId, title)).thenReturn(books);
    when(bookMapper.toDTO(book)).thenReturn(bookDTO);

    // Act
    List<BookDTO> result = searchService.advancedSearch(categoryName, authorName, userId, title);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(bookRepository, times(1)).advancedSearch(categoryName, authorName, userId, title);
    verify(bookMapper, times(1)).toDTO(book);
  }

  @Test
  @DisplayName("UC-SEARCH-001: shouldAdvancedSearch_WhenNoParameters")
  void shouldAdvancedSearch_WhenNoParameters() {
    // Arrange
    List<Book> books = List.of(book);
    when(bookRepository.advancedSearch(null, null, null, null)).thenReturn(books);
    when(bookMapper.toDTO(book)).thenReturn(bookDTO);

    // Act
    List<BookDTO> result = searchService.advancedSearch(null, null, null, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(bookRepository, times(1)).advancedSearch(null, null, null, null);
  }

  @Test
  @DisplayName("UC-SEARCH-002: shouldSearchBooks_WithPagination")
  void shouldSearchBooks_WithPagination() {
    // Arrange
    BookSearchCriteriaDTO criteria = new BookSearchCriteriaDTO();
    criteria.setTitle("Test");
    criteria.setAuthor("John");
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books = List.of(book);
    Page<Book> bookPage = new PageImpl<>(books, pageable, 1);

    when(bookRepository.searchBooks(
        criteria.getTitle(),
        criteria.getAuthor(),
        criteria.getCategory(),
        criteria.getPublisher(),
        criteria.getIsbn(),
        criteria.getMinYear(),
        criteria.getMaxYear(),
        pageable
    )).thenReturn(bookPage);
    when(bookMapper.toDTO(book)).thenReturn(bookDTO);

    // Act
    Page<BookDTO> result = searchService.searchBooks(criteria, pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(bookRepository, times(1)).searchBooks(
        criteria.getTitle(),
        criteria.getAuthor(),
        criteria.getCategory(),
        criteria.getPublisher(),
        criteria.getIsbn(),
        criteria.getMinYear(),
        criteria.getMaxYear(),
        pageable
    );
    verify(bookMapper, times(1)).toDTO(book);
  }

  @Test
  @DisplayName("UC-SEARCH-002: shouldSearchBooks_WithAllCriteria")
  void shouldSearchBooks_WithAllCriteria() {
    // Arrange
    BookSearchCriteriaDTO criteria = new BookSearchCriteriaDTO();
    criteria.setTitle("Test");
    criteria.setIsbn("1234567890");
    criteria.setAuthor("John");
    criteria.setCategory("Fiction");
    criteria.setPublisher("Test Publisher");
    criteria.setMinYear(2020);
    criteria.setMaxYear(2024);
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books = List.of(book);
    Page<Book> bookPage = new PageImpl<>(books, pageable, 1);

    when(bookRepository.searchBooks(
        criteria.getTitle(),
        criteria.getAuthor(),
        criteria.getCategory(),
        criteria.getPublisher(),
        criteria.getIsbn(),
        criteria.getMinYear(),
        criteria.getMaxYear(),
        pageable
    )).thenReturn(bookPage);
    when(bookMapper.toDTO(book)).thenReturn(bookDTO);

    // Act
    Page<BookDTO> result = searchService.searchBooks(criteria, pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(bookRepository, times(1)).searchBooks(
        criteria.getTitle(),
        criteria.getAuthor(),
        criteria.getCategory(),
        criteria.getPublisher(),
        criteria.getIsbn(),
        criteria.getMinYear(),
        criteria.getMaxYear(),
        pageable
    );
  }
}
