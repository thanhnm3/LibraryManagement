package com.example.demo.controller;

import com.example.demo.dto.review.AverageRatingDTO;
import com.example.demo.dto.review.ReviewDTO;
import com.example.demo.dto.review.ReviewRequestDTO;
import com.example.demo.dto.review.ReviewUpdateDTO;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	/**
	 * UC-REVIEW-001: Tạo đánh giá sách
	 */
	@PostMapping
	public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
		ReviewDTO review = reviewService.createReview(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(review);
	}

	/**
	 * UC-REVIEW-002: Cập nhật đánh giá
	 */
	@PutMapping("/{reviewId}")
	public ResponseEntity<ReviewDTO> updateReview(
			@PathVariable Long reviewId,
			@Valid @RequestBody ReviewUpdateDTO request,
			@RequestParam Long userId) {
		ReviewDTO review = reviewService.updateReview(reviewId, request, userId);
		return ResponseEntity.ok(review);
	}

	/**
	 * UC-REVIEW-003: Xóa đánh giá
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> deleteReview(
			@PathVariable Long reviewId,
			@RequestParam Long userId,
			@RequestParam(defaultValue = "false") boolean isAdmin) {
		reviewService.deleteReview(reviewId, userId, isAdmin);
		return ResponseEntity.noContent().build();
	}

	/**
	 * UC-REVIEW-004: Lấy danh sách đánh giá của sách
	 */
	@GetMapping("/books/{bookId}")
	public ResponseEntity<Page<ReviewDTO>> getReviewsByBookId(
			@PathVariable Long bookId,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<ReviewDTO> reviews = reviewService.getReviewsByBookId(bookId, pageable);
		return ResponseEntity.ok(reviews);
	}

	/**
	 * UC-REVIEW-005: Lấy đánh giá của user
	 */
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
		List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
		return ResponseEntity.ok(reviews);
	}

	/**
	 * UC-REVIEW-006: Lấy điểm đánh giá trung bình của sách
	 */
	@GetMapping("/books/{bookId}/average-rating")
	public ResponseEntity<AverageRatingDTO> getAverageRatingByBookId(@PathVariable Long bookId) {
		AverageRatingDTO rating = reviewService.getAverageRatingByBookId(bookId);
		return ResponseEntity.ok(rating);
	}
}
