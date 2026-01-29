package com.example.demo.repository;

import com.example.demo.entity.Loan;
import com.example.demo.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

	Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

	@Query("SELECT l FROM Loan l WHERE l.user.id = :userId")
	Page<Loan> findByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query("SELECT l FROM Loan l WHERE l.book.id = :bookId")
	Page<Loan> findByBookId(@Param("bookId") Long bookId, Pageable pageable);

	@Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.status = :status")
	Page<Loan> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") LoanStatus status, Pageable pageable);

	@Query("SELECT l FROM Loan l WHERE l.user.id = :userId")
	List<Loan> findListByUserId(@Param("userId") Long userId);

	@Query("SELECT l FROM Loan l WHERE l.book.id = :bookId")
	List<Loan> findListByBookId(@Param("bookId") Long bookId);

	@Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.status = :status")
	List<Loan> findListByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") LoanStatus status);

	@Query("SELECT l FROM Loan l WHERE l.status = :status AND l.dueDate < :currentDate")
	List<Loan> findOverdueLoans(@Param("currentDate") LocalDateTime currentDate, @Param("status") LoanStatus status);

	@Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.status = :status AND l.dueDate < :currentDate")
	List<Loan> findOverdueLoansByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDateTime currentDate, @Param("status") LoanStatus status);

	@Query("SELECT COUNT(l) FROM Loan l WHERE l.book.id = :bookId AND l.status = :status")
	Long countActiveLoansByBookId(@Param("bookId") Long bookId, @Param("status") LoanStatus status);

	@Query("SELECT COUNT(l) FROM Loan l WHERE l.user.id = :userId AND l.status = :status")
	Long countActiveLoansByUserId(@Param("userId") Long userId, @Param("status") LoanStatus status);

	@Query("SELECT COUNT(l) FROM Loan l WHERE l.status = :status")
	Long countByStatus(@Param("status") LoanStatus status);

	@Query("SELECT l FROM Loan l WHERE " +
			"(:startDate IS NULL OR l.borrowDate >= :startDate) AND " +
			"(:endDate IS NULL OR l.borrowDate <= :endDate)")
	List<Loan> findLoansByDateRange(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate
	);

	@Query("SELECT COUNT(l) FROM Loan l WHERE " +
			"(:startDate IS NULL OR l.borrowDate >= :startDate) AND " +
			"(:endDate IS NULL OR l.borrowDate <= :endDate) AND " +
			"l.status = :status")
	Long countLoansByDateRangeAndStatus(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate,
			@Param("status") LoanStatus status
	);
}
