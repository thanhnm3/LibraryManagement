package com.example.demo.repository;

import com.example.demo.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

	Page<Author> findAll(Pageable pageable);

	@Query("SELECT a FROM Author a WHERE LOWER(a.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<Author> findByFullNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

	@Query("SELECT a FROM Author a WHERE " +
			"(:search IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<Author> findAllWithSearch(@Param("search") String search, Pageable pageable);
}
