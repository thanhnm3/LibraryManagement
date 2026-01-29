package com.example.demo.service.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.author.AuthorDetailDTO;
import com.example.demo.dto.author.AuthorRequestDTO;
import com.example.demo.dto.author.AuthorUpdateDTO;
import com.example.demo.entity.Author;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.repository.AuthorRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for AuthorServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthorService Tests")
class AuthorServiceImplTest {

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private AuthorMapper authorMapper;

  @InjectMocks
  private AuthorServiceImpl authorService;

  private Author testAuthor;
  private AuthorDTO testAuthorDTO;
  private AuthorDetailDTO testAuthorDetailDTO;
  private AuthorRequestDTO testAuthorRequestDTO;
  private AuthorUpdateDTO testAuthorUpdateDTO;
  private Long testAuthorId;

  @BeforeEach
  void setUp() {
    testAuthorId = 1L;
    testAuthor = TestDataBuilder.createAuthor();
    testAuthorDTO = TestDataBuilder.createAuthorDTO();
    testAuthorDetailDTO = TestDataBuilder.createAuthorDetailDTO();
    testAuthorRequestDTO = TestDataBuilder.createAuthorRequestDTO();
    testAuthorUpdateDTO = TestDataBuilder.createAuthorUpdateDTO();
  }

  /**
   * UC-AUTHOR-001: createAuthor
   * Test: shouldCreateAuthor_WhenValidRequest
   */
  @Test
  @DisplayName("Should create author when valid request")
  void shouldCreateAuthor_WhenValidRequest() {
    // Arrange
    when(authorMapper.toEntity(testAuthorRequestDTO)).thenReturn(testAuthor);
    when(authorRepository.save(testAuthor)).thenReturn(testAuthor);
    when(authorMapper.toDTO(testAuthor)).thenReturn(testAuthorDTO);

    // Act
    AuthorDTO result = authorService.createAuthor(testAuthorRequestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(testAuthorDTO.getId(), result.getId());
    assertEquals(testAuthorDTO.getFullName(), result.getFullName());
    verify(authorMapper, times(1)).toEntity(testAuthorRequestDTO);
    verify(authorRepository, times(1)).save(testAuthor);
    verify(authorMapper, times(1)).toDTO(testAuthor);
  }

  /**
   * UC-AUTHOR-002: getAllAuthors
   * Test: shouldGetAllAuthors_WhenNoSearch
   */
  @Test
  @DisplayName("Should get all authors when no search term")
  void shouldGetAllAuthors_WhenNoSearch() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Author> authors = List.of(testAuthor);
    Page<Author> authorPage = new PageImpl<>(authors, pageable, 1);

    when(authorRepository.findAllWithSearch(null, pageable)).thenReturn(authorPage);
    when(authorMapper.toDTO(testAuthor)).thenReturn(testAuthorDTO);

    // Act
    Page<AuthorDTO> result = authorService.getAllAuthors(pageable, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(authorRepository, times(1)).findAllWithSearch(null, pageable);
    verify(authorMapper, times(1)).toDTO(testAuthor);
  }

  /**
   * UC-AUTHOR-002: getAllAuthors
   * Test: shouldGetAllAuthors_WhenWithSearch
   */
  @Test
  @DisplayName("Should get all authors when with search term")
  void shouldGetAllAuthors_WhenWithSearch() {
    // Arrange
    String searchTerm = "test";
    Pageable pageable = PageRequest.of(0, 10);
    List<Author> authors = List.of(testAuthor);
    Page<Author> authorPage = new PageImpl<>(authors, pageable, 1);

    when(authorRepository.findAllWithSearch(searchTerm, pageable)).thenReturn(authorPage);
    when(authorMapper.toDTO(testAuthor)).thenReturn(testAuthorDTO);

    // Act
    Page<AuthorDTO> result = authorService.getAllAuthors(pageable, searchTerm);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(authorRepository, times(1)).findAllWithSearch(searchTerm, pageable);
    verify(authorMapper, times(1)).toDTO(testAuthor);
  }

  /**
   * UC-AUTHOR-002: getAllAuthors
   * Test: shouldGetAllAuthors_WhenSearchIsEmpty
   */
  @Test
  @DisplayName("Should get all authors when search term is empty")
  void shouldGetAllAuthors_WhenSearchIsEmpty() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Author> authors = List.of(testAuthor);
    Page<Author> authorPage = new PageImpl<>(authors, pageable, 1);

    when(authorRepository.findAllWithSearch(null, pageable)).thenReturn(authorPage);
    when(authorMapper.toDTO(testAuthor)).thenReturn(testAuthorDTO);

    // Act
    Page<AuthorDTO> result = authorService.getAllAuthors(pageable, "   ");

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(authorRepository, times(1)).findAllWithSearch(null, pageable);
  }

  /**
   * UC-AUTHOR-003: getAuthorById
   * Test: shouldGetAuthorById_WhenExists
   */
  @Test
  @DisplayName("Should get author by id when exists")
  void shouldGetAuthorById_WhenExists() {
    // Arrange
    when(authorRepository.findById(testAuthorId)).thenReturn(Optional.of(testAuthor));
    when(authorMapper.toDetailDTO(testAuthor)).thenReturn(testAuthorDetailDTO);

    // Act
    AuthorDetailDTO result = authorService.getAuthorById(testAuthorId);

    // Assert
    assertNotNull(result);
    assertEquals(testAuthorDetailDTO.getId(), result.getId());
    assertEquals(testAuthorDetailDTO.getFullName(), result.getFullName());
    verify(authorRepository, times(1)).findById(testAuthorId);
    verify(authorMapper, times(1)).toDetailDTO(testAuthor);
  }

  /**
   * UC-AUTHOR-003: getAuthorById
   * Test: shouldThrowException_WhenAuthorNotFound
   */
  @Test
  @DisplayName("Should throw exception when author not found")
  void shouldThrowException_WhenAuthorNotFound() {
    // Arrange
    when(authorRepository.findById(testAuthorId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      authorService.getAuthorById(testAuthorId);
    });

    verify(authorRepository, times(1)).findById(testAuthorId);
    verify(authorMapper, never()).toDetailDTO(any());
  }

  /**
   * UC-AUTHOR-004: updateAuthor
   * Test: shouldUpdateAuthor_WhenValidRequest
   */
  @Test
  @DisplayName("Should update author when valid request")
  void shouldUpdateAuthor_WhenValidRequest() {
    // Arrange
    when(authorRepository.findById(testAuthorId)).thenReturn(Optional.of(testAuthor));
    when(authorRepository.save(testAuthor)).thenReturn(testAuthor);
    when(authorMapper.toDTO(testAuthor)).thenReturn(testAuthorDTO);

    // Act
    AuthorDTO result = authorService.updateAuthor(testAuthorId, testAuthorUpdateDTO);

    // Assert
    assertNotNull(result);
    verify(authorRepository, times(1)).findById(testAuthorId);
    verify(authorMapper, times(1)).updateEntityFromDTO(testAuthor, testAuthorUpdateDTO);
    verify(authorRepository, times(1)).save(testAuthor);
    verify(authorMapper, times(1)).toDTO(testAuthor);
  }

  /**
   * UC-AUTHOR-004: updateAuthor
   * Test: shouldThrowException_WhenAuthorNotFoundForUpdate
   */
  @Test
  @DisplayName("Should throw exception when author not found for update")
  void shouldThrowException_WhenAuthorNotFoundForUpdate() {
    // Arrange
    when(authorRepository.findById(testAuthorId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      authorService.updateAuthor(testAuthorId, testAuthorUpdateDTO);
    });

    verify(authorRepository, times(1)).findById(testAuthorId);
    verify(authorMapper, never()).updateEntityFromDTO(any(), any());
    verify(authorRepository, never()).save(any());
  }

  /**
   * UC-AUTHOR-005: deleteAuthor
   * Test: shouldDeleteAuthor_WhenNoBooks
   */
  @Test
  @DisplayName("Should delete author when no books")
  void shouldDeleteAuthor_WhenNoBooks() {
    // Arrange
    testAuthor.setBooks(new ArrayList<>());
    when(authorRepository.findById(testAuthorId)).thenReturn(Optional.of(testAuthor));

    // Act
    authorService.deleteAuthor(testAuthorId);

    // Assert
    verify(authorRepository, times(1)).findById(testAuthorId);
    verify(authorRepository, times(1)).delete(testAuthor);
  }

  /**
   * UC-AUTHOR-005: deleteAuthor
   * Test: shouldThrowException_WhenAuthorHasBooks
   */
  @Test
  @DisplayName("Should throw exception when author has books")
  void shouldThrowException_WhenAuthorHasBooks() {
    // Arrange
    List<com.example.demo.entity.Book> books = List.of(TestDataBuilder.createBook());
    testAuthor.setBooks(books);
    when(authorRepository.findById(testAuthorId)).thenReturn(Optional.of(testAuthor));

    // Act & Assert
    assertThrows(BusinessRuleException.class, () -> {
      authorService.deleteAuthor(testAuthorId);
    });

    verify(authorRepository, times(1)).findById(testAuthorId);
    verify(authorRepository, never()).delete(any());
  }
}
