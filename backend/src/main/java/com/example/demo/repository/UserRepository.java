package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	List<User> findByPasswordHash(String passwordHash);

	@Query("SELECT u FROM User u WHERE " +
		"(:status IS NULL OR u.status = :status) " +
		"AND (:role IS NULL OR u.role = :role)")
	Page<User> findAllWithFilters(
		@Param("status") UserStatus status,
		@Param("role") UserRole role,
		Pageable pageable
	);
}
