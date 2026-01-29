package com.example.demo.repository;

import com.example.demo.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Optional<Publisher> findByName(String name);

	Page<Publisher> findAll(Pageable pageable);
}
