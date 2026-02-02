package com.example.demo.service;

import com.example.demo.dto.review.AverageRatingDTO;
import com.example.demo.dto.review.ReviewDTO;
import com.example.demo.dto.review.ReviewRequestDTO;
import com.example.demo.dto.review.ReviewUpdateDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Review operations (UC-REVIEW-001 ~ UC-REVIEW-006)
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final ReviewMapper reviewMapper;

	public ReviewServiceImpl(
			ReviewRepository reviewRepository,
			UserRepository userRepository,
			BookRepository bookRepository,
			ReviewMapper reviewMapper) {
		this.reviewRepository = reviewRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.reviewMapper = reviewMapper;
	}

	@Override
	public ReviewDTO createReview(ReviewRequestDTO request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
		Book book = bookRepository.findById(request.getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

		if (reviewRepository.findByUserIdAndBookId(request.getUserId(), request.getBookId()).isPresent()) {
			throw new DuplicateResourceException("User has already reviewed this book");
		}

		Review review = reviewMapper.toEntity(request);
		review.setUser(user);
		review.setBook(book);
		Review savedReview = reviewRepository.save(review);
		return reviewMapper.toDTO(savedReview);
	}

	@Override
	public ReviewDTO updateReview(Long reviewId, ReviewUpdateDTO request, Long userId, boolean isAdmin) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

		boolean isOwner = review.getUser().getId().equals(userId);
		if (!isOwner && !isAdmin) {
			throw new BusinessException("User is not the owner of this review");
		}

		reviewMapper.updateEntityFromDTO(review, request);
		Review updatedReview = reviewRepository.save(review);
		return reviewMapper.toDTO(updatedReview);
	}

	@Override
	public void deleteReview(Long reviewId, Long userId, boolean isAdmin) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

		boolean isOwner = review.getUser().getId().equals(userId);
		if (!isOwner && !isAdmin) {
			throw new BusinessException("User is not the owner and not admin");
		}

		reviewRepository.delete(review);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReviewDTO> getReviewsByBookId(Long bookId, Pageable pageable) {
		return reviewRepository.findByBookId(bookId, pageable).map(reviewMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewDTO> getReviewsByUserId(Long userId) {
		return reviewRepository.findByUserId(userId).stream()
				.map(reviewMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public AverageRatingDTO getAverageRatingByBookId(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

		Double averageRating = reviewRepository.getAverageRatingByBookId(bookId);
		Long totalReviews = reviewRepository.countReviewsByBookId(bookId);

		AverageRatingDTO dto = new AverageRatingDTO();
		dto.setBookId(bookId);
		dto.setBookTitle(book.getTitle());
		dto.setAverageRating(averageRating != null ? averageRating : 0.0);
		dto.setTotalReviews(totalReviews != null ? totalReviews : 0L);
		return dto;
	}
}
