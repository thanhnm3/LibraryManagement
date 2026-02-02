package com.example.demo.service;

import com.example.demo.dto.review.AverageRatingDTO;
import com.example.demo.dto.review.ReviewDTO;
import com.example.demo.dto.review.ReviewRequestDTO;
import com.example.demo.dto.review.ReviewUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Review operations
 */
public interface ReviewService {

	/**
	 * UC-REVIEW-001: Tạo đánh giá sách
	 * Tạo review mới cho sách
	 * 
	 * @param request - Thông tin review
	 * @return ReviewDTO - Thông tin review đã tạo
	 */
	ReviewDTO createReview(ReviewRequestDTO request);

	/**
	 * UC-REVIEW-002: Cập nhật đánh giá (owner hoặc admin)
	 *
	 * @param reviewId - ID của review
	 * @param request  - Thông tin cập nhật
	 * @param userId   - ID của user thực hiện (để kiểm tra quyền)
	 * @param isAdmin  - Có phải admin không
	 * @return ReviewDTO - Thông tin review đã cập nhật
	 */
	ReviewDTO updateReview(Long reviewId, ReviewUpdateDTO request, Long userId, boolean isAdmin);

	/**
	 * UC-REVIEW-003: Xóa đánh giá
	 * Xóa review
	 * 
	 * @param reviewId - ID của review
	 * @param userId   - ID của user (để kiểm tra quyền)
	 * @param isAdmin  - Có phải admin không
	 */
	void deleteReview(Long reviewId, Long userId, boolean isAdmin);

	/**
	 * UC-REVIEW-004: Lấy danh sách đánh giá của sách
	 * Lấy danh sách review của một sách
	 * 
	 * @param bookId   - ID của sách
	 * @param pageable - Pagination info
	 * @return Page<ReviewDTO> - Danh sách review
	 */
	Page<ReviewDTO> getReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * UC-REVIEW-005: Lấy đánh giá của user
	 * Lấy danh sách review của một user
	 * 
	 * @param userId - ID của user
	 * @return List<ReviewDTO> - Danh sách review
	 */
	List<ReviewDTO> getReviewsByUserId(Long userId);

	/**
	 * UC-REVIEW-006: Lấy điểm đánh giá trung bình của sách
	 * 
	 * @param bookId - ID của sách
	 * @return AverageRatingDTO - Thông tin rating trung bình
	 */
	AverageRatingDTO getAverageRatingByBookId(Long bookId);
}
