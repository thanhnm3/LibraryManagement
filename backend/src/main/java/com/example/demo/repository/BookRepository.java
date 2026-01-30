package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findByIsbn(String isbn);

	@Query("SELECT DISTINCT b FROM Book b " +
			"LEFT JOIN b.authors a " +
			"LEFT JOIN b.categories c " +
			"LEFT JOIN b.publisher p " +
			"WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
			"AND (:authorName IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :authorName, '%'))) " +
			"AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
			"AND (:publisherName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :publisherName, '%'))) " +
			"AND (:isbn IS NULL OR b.isbn = :isbn) " +
			"AND (:minYear IS NULL OR b.publicationYear >= :minYear) " +
			"AND (:maxYear IS NULL OR b.publicationYear <= :maxYear)")
	Page<Book> searchBooks(
			@Param("title") String title,
			@Param("authorName") String authorName,
			@Param("categoryName") String categoryName,
			@Param("publisherName") String publisherName,
			@Param("isbn") String isbn,
			@Param("minYear") Integer minYear,
			@Param("maxYear") Integer maxYear,
			Pageable pageable
	);

	@Query("SELECT DISTINCT b FROM Book b " +
			"LEFT JOIN b.authors a " +
			"LEFT JOIN b.categories c " +
			"LEFT JOIN b.loans l " +
			"WHERE (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
			"AND (:authorName IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :authorName, '%'))) " +
			"AND (:userId IS NULL OR (l.user.id = :userId AND l.status = 'BORROWED')) " +
			"AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))")
	List<Book> advancedSearch(
			@Param("categoryName") String categoryName,
			@Param("authorName") String authorName,
			@Param("userId") Long userId,
			@Param("title") String title
	);

	@Query("SELECT b, COUNT(l) as loanCount FROM Book b " +
			"LEFT JOIN b.loans l " +
			"GROUP BY b.id " +
			"ORDER BY loanCount DESC")
	List<Object[]> findMostBorrowedBooks(Pageable pageable);

	@Query("SELECT b FROM Book b " +
			"WHERE b.id IN (" +
			"  SELECT r.book.id FROM Review r " +
			"  GROUP BY r.book.id " +
			"  HAVING AVG(r.rating) >= :minRating" +
			") " +
			"ORDER BY (SELECT AVG(r2.rating) FROM Review r2 WHERE r2.book.id = b.id) DESC")
	List<Book> findTopRatedBooks(@Param("minRating") Double minRating, Pageable pageable);
}
