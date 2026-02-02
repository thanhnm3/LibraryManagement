package com.example.demo.controller;

import com.example.demo.dto.publisher.PublisherDTO;
import com.example.demo.dto.publisher.PublisherDetailDTO;
import com.example.demo.dto.publisher.PublisherRequestDTO;
import com.example.demo.dto.publisher.PublisherUpdateDTO;
import com.example.demo.service.publisher.PublisherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

	private final PublisherService publisherService;

	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	/**
	 * UC-PUBLISHER-001: Tạo nhà xuất bản mới
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherRequestDTO requestDTO) {
		PublisherDTO createdPublisher = publisherService.createPublisher(requestDTO);
		return new ResponseEntity<>(createdPublisher, HttpStatus.CREATED);
	}

	/**
	 * UC-PUBLISHER-002: Lấy danh sách nhà xuất bản
	 */
	@GetMapping
	public ResponseEntity<Page<PublisherDTO>> getAllPublishers(
		@PageableDefault(size = 20) Pageable pageable
	) {
		Page<PublisherDTO> publishers = publisherService.getAllPublishers(pageable);
		return ResponseEntity.ok(publishers);
	}

	/**
	 * UC-PUBLISHER-003: Lấy chi tiết nhà xuất bản
	 */
	@GetMapping("/{id}")
	public ResponseEntity<PublisherDetailDTO> getPublisherById(@PathVariable Long id) {
		PublisherDetailDTO publisher = publisherService.getPublisherById(id);
		return ResponseEntity.ok(publisher);
	}

	/**
	 * UC-PUBLISHER-004: Cập nhật thông tin nhà xuất bản
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PublisherDTO> updatePublisher(
		@PathVariable Long id,
		@Valid @RequestBody PublisherUpdateDTO updateDTO
	) {
		PublisherDTO updatedPublisher = publisherService.updatePublisher(id, updateDTO);
		return ResponseEntity.ok(updatedPublisher);
	}

	/**
	 * UC-PUBLISHER-005: Xóa nhà xuất bản
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
		publisherService.deletePublisher(id);
		return ResponseEntity.noContent().build();
	}
}
