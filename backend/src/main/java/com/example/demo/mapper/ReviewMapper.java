package com.example.demo.mapper;

import com.example.demo.dto.review.ReviewDTO;
import com.example.demo.dto.review.ReviewRequestDTO;
import com.example.demo.dto.review.ReviewUpdateDTO;
import com.example.demo.entity.Review;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Review entity and DTOs
 */
@Component
public class ReviewMapper {

	/**
	 * Convert Review entity to ReviewDTO
	 * 
	 * @param review - Review entity
	 * @return ReviewDTO
	 */
	public ReviewDTO toDTO(Review review) {
		if (review == null) {
			return null;
		}

		ReviewDTO dto = new ReviewDTO();
		dto.setId(review.getId());
		dto.setUserId(review.getUser().getId());
		dto.setUserFullName(review.getUser().getFullName());
		dto.setBookId(review.getBook().getId());
		dto.setBookTitle(review.getBook().getTitle());
		dto.setRating(review.getRating());
		dto.setComment(review.getComment());
		dto.setCreatedAt(review.getCreatedAt());
		return dto;
	}

	/**
	 * Convert ReviewRequestDTO to Review entity
	 * Note: user and book should be set separately in service
	 * 
	 * @param requestDTO - ReviewRequestDTO
	 * @return Review entity
	 */
	public Review toEntity(ReviewRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		Review review = new Review();
		review.setRating(requestDTO.getRating());
		review.setComment(requestDTO.getComment());
		return review;
	}

	/**
	 * Update Review entity from ReviewUpdateDTO
	 * 
	 * @param review   - Review entity to update
	 * @param updateDTO - ReviewUpdateDTO with new values
	 */
	public void updateEntityFromDTO(Review review, ReviewUpdateDTO updateDTO) {
		if (review == null || updateDTO == null) {
			return;
		}

		if (updateDTO.getRating() != null) {
			review.setRating(updateDTO.getRating());
		}

		if (updateDTO.getComment() != null) {
			review.setComment(updateDTO.getComment());
		}
	}
}
