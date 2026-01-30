package com.example.demo.service.review;

import com.example.demo.dto.review.AverageRatingDTO;
import com.example.demo.dto.review.ReviewDTO;
import com.example.demo.dto.review.ReviewRequestDTO;
import com.example.demo.dto.review.ReviewUpdateDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReviewServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test class for ReviewService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Tests")
class ReviewServiceImplTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private ReviewMapper reviewMapper;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  private ReviewRequestDTO reviewRequestDTO;
  private Review review;
  private ReviewDTO reviewDTO;
  private User user;
  private Book book;

  @BeforeEach
  void setUp() {
    reviewRequestDTO = TestDataBuilder.createReviewRequestDTO();
    review = TestDataBuilder.createReview();
    reviewDTO = TestDataBuilder.createReviewDTO();
    user = TestDataBuilder.createUser();
    book = TestDataBuilder.createBook();
  }

  @Test
  @DisplayName("UC-REVIEW-001: shouldCreateReview_WhenValidRequest")
  void shouldCreateReview_WhenValidRequest() {
    // Arrange
    when(userRepository.findById(reviewRequestDTO.getUserId())).thenReturn(Optional.of(user));
    when(bookRepository.findById(reviewRequestDTO.getBookId())).thenReturn(Optional.of(book));
    when(reviewRepository.findByUserIdAndBookId(reviewRequestDTO.getUserId(), reviewRequestDTO.getBookId()))
        .thenReturn(Optional.empty());
    when(reviewMapper.toEntity(reviewRequestDTO)).thenReturn(review);
    when(reviewRepository.save(any(Review.class))).thenReturn(review);
    when(reviewMapper.toDTO(review)).thenReturn(reviewDTO);

    // Act
    ReviewDTO result = reviewService.createReview(reviewRequestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(reviewDTO.getId(), result.getId());
    verify(userRepository, times(1)).findById(reviewRequestDTO.getUserId());
    verify(bookRepository, times(1)).findById(reviewRequestDTO.getBookId());
    verify(reviewRepository, times(1)).findByUserIdAndBookId(reviewRequestDTO.getUserId(), reviewRequestDTO.getBookId());
    verify(reviewRepository, times(1)).save(any(Review.class));
    verify(reviewMapper, times(1)).toDTO(review);
  }

  @Test
  @DisplayName("UC-REVIEW-001: shouldThrowException_WhenReviewAlreadyExists")
  void shouldThrowException_WhenReviewAlreadyExists() {
    // Arrange
    when(userRepository.findById(reviewRequestDTO.getUserId())).thenReturn(Optional.of(user));
    when(bookRepository.findById(reviewRequestDTO.getBookId())).thenReturn(Optional.of(book));
    when(reviewRepository.findByUserIdAndBookId(reviewRequestDTO.getUserId(), reviewRequestDTO.getBookId()))
        .thenReturn(Optional.of(review));

    // Act & Assert
    assertThrows(DuplicateResourceException.class, () -> reviewService.createReview(reviewRequestDTO));
    verify(reviewRepository, never()).save(any(Review.class));
  }

  @Test
  @DisplayName("UC-REVIEW-002: shouldUpdateReview_WhenUserIsOwner")
  void shouldUpdateReview_WhenUserIsOwner() {
    // Arrange
    Long reviewId = 1L;
    Long userId = 1L;
    ReviewUpdateDTO updateDTO = new ReviewUpdateDTO();
    updateDTO.setRating(4);
    updateDTO.setComment("Updated comment");

    Review existingReview = TestDataBuilder.createReviewWithUserId(userId);
    Review updatedReview = TestDataBuilder.createReviewWithUserId(userId);
    updatedReview.setRating(4);
    updatedReview.setComment("Updated comment");

    ReviewDTO updatedDTO = TestDataBuilder.createReviewDTO();
    updatedDTO.setRating(4);
    updatedDTO.setComment("Updated comment");

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
    when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);
    when(reviewMapper.toDTO(updatedReview)).thenReturn(updatedDTO);

    // Act
    ReviewDTO result = reviewService.updateReview(reviewId, updateDTO, userId);

    // Assert
    assertNotNull(result);
    assertEquals(4, result.getRating());
    assertEquals("Updated comment", result.getComment());
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewMapper, times(1)).updateEntityFromDTO(eq(existingReview), eq(updateDTO));
    verify(reviewRepository, times(1)).save(existingReview);
  }

  @Test
  @DisplayName("UC-REVIEW-002: shouldThrowException_WhenUserNotOwner")
  void shouldThrowException_WhenUserNotOwner() {
    // Arrange
    Long reviewId = 1L;
    Long userId = 1L;
    Long differentUserId = 2L;
    ReviewUpdateDTO updateDTO = new ReviewUpdateDTO();
    updateDTO.setRating(4);

    Review existingReview = TestDataBuilder.createReviewWithUserId(differentUserId);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

    // Act & Assert
    assertThrows(BusinessException.class, () -> reviewService.updateReview(reviewId, updateDTO, userId));
    verify(reviewRepository, never()).save(any(Review.class));
  }

  @Test
  @DisplayName("UC-REVIEW-003: shouldDeleteReview_WhenUserIsOwner")
  void shouldDeleteReview_WhenUserIsOwner() {
    // Arrange
    Long reviewId = 1L;
    Long userId = 1L;
    Review existingReview = TestDataBuilder.createReviewWithUserId(userId);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

    // Act
    reviewService.deleteReview(reviewId, userId, false);

    // Assert
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewRepository, times(1)).delete(existingReview);
  }

  @Test
  @DisplayName("UC-REVIEW-003: shouldDeleteReview_WhenIsAdmin")
  void shouldDeleteReview_WhenIsAdmin() {
    // Arrange
    Long reviewId = 1L;
    Long userId = 1L;
    Long differentUserId = 2L;
    Review existingReview = TestDataBuilder.createReviewWithUserId(differentUserId);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

    // Act
    reviewService.deleteReview(reviewId, userId, true);

    // Assert
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewRepository, times(1)).delete(existingReview);
  }

  @Test
  @DisplayName("UC-REVIEW-003: shouldThrowException_WhenUserNotOwnerAndNotAdmin")
  void shouldThrowException_WhenUserNotOwnerAndNotAdmin() {
    // Arrange
    Long reviewId = 1L;
    Long userId = 1L;
    Long differentUserId = 2L;
    Review existingReview = TestDataBuilder.createReviewWithUserId(differentUserId);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

    // Act & Assert
    assertThrows(BusinessException.class, () -> reviewService.deleteReview(reviewId, userId, false));
    verify(reviewRepository, never()).delete(any(Review.class));
  }

  @Test
  @DisplayName("UC-REVIEW-004: shouldGetReviewsByBookId_WithPagination")
  void shouldGetReviewsByBookId_WithPagination() {
    // Arrange
    Long bookId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    List<Review> reviews = new ArrayList<>();
    reviews.add(review);
    Page<Review> reviewPage = new PageImpl<>(reviews, pageable, 1);

    when(reviewRepository.findByBookId(bookId, pageable)).thenReturn(reviewPage);
    when(reviewMapper.toDTO(any(Review.class))).thenReturn(reviewDTO);

    // Act
    Page<ReviewDTO> result = reviewService.getReviewsByBookId(bookId, pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(reviewRepository, times(1)).findByBookId(bookId, pageable);
    verify(reviewMapper, times(1)).toDTO(review);
  }

  @Test
  @DisplayName("UC-REVIEW-005: shouldGetReviewsByUserId_WhenExists")
  void shouldGetReviewsByUserId_WhenExists() {
    // Arrange
    Long userId = 1L;
    List<Review> reviews = new ArrayList<>();
    reviews.add(review);

    when(reviewRepository.findByUserId(userId)).thenReturn(reviews);
    when(reviewMapper.toDTO(any(Review.class))).thenReturn(reviewDTO);

    // Act
    List<ReviewDTO> result = reviewService.getReviewsByUserId(userId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(reviewRepository, times(1)).findByUserId(userId);
    verify(reviewMapper, times(1)).toDTO(review);
  }

  @Test
  @DisplayName("UC-REVIEW-006: shouldGetAverageRating_WhenHasReviews")
  void shouldGetAverageRating_WhenHasReviews() {
    // Arrange
    Long bookId = 1L;
    Double averageRating = 4.5;
    Long totalReviews = 10L;

    when(reviewRepository.getAverageRatingByBookId(bookId)).thenReturn(averageRating);
    when(reviewRepository.countReviewsByBookId(bookId)).thenReturn(totalReviews);
    when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

    // Act
    AverageRatingDTO result = reviewService.getAverageRatingByBookId(bookId);

    // Assert
    assertNotNull(result);
    assertEquals(averageRating, result.getAverageRating());
    assertEquals(totalReviews, result.getTotalReviews());
    verify(reviewRepository, times(1)).getAverageRatingByBookId(bookId);
    verify(reviewRepository, times(1)).countReviewsByBookId(bookId);
  }

  @Test
  @DisplayName("UC-REVIEW-006: shouldReturnZero_WhenNoReviews")
  void shouldReturnZero_WhenNoReviews() {
    // Arrange
    Long bookId = 1L;

    when(reviewRepository.getAverageRatingByBookId(bookId)).thenReturn(null);
    when(reviewRepository.countReviewsByBookId(bookId)).thenReturn(0L);
    when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

    // Act
    AverageRatingDTO result = reviewService.getAverageRatingByBookId(bookId);

    // Assert
    assertNotNull(result);
    assertEquals(0.0, result.getAverageRating());
    assertEquals(0L, result.getTotalReviews());
  }
}
