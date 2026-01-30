package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query("SELECT r FROM Review r WHERE r.book.id = :bookId")
	Page<Review> findByBookId(@Param("bookId") Long bookId, Pageable pageable);

	@Query("SELECT r FROM Review r WHERE r.book.id = :bookId")
	List<Review> findListByBookId(@Param("bookId") Long bookId);

	@Query("SELECT r FROM Review r WHERE r.user.id = :userId")
	List<Review> findByUserId(@Param("userId") Long userId);

	@Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.book.id = :bookId")
	Optional<Review> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
	Double getAverageRatingByBookId(@Param("bookId") Long bookId);

	@Query("SELECT COUNT(r) FROM Review r WHERE r.book.id = :bookId")
	Long countReviewsByBookId(@Param("bookId") Long bookId);
}
