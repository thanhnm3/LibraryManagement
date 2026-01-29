package com.example.demo.controller;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.author.AuthorDetailDTO;
import com.example.demo.dto.author.AuthorRequestDTO;
import com.example.demo.dto.author.AuthorUpdateDTO;
import com.example.demo.service.author.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

	private final AuthorService authorService;

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	/**
	 * UC-AUTHOR-001: Tạo tác giả mới
	 */
	@PostMapping
	public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO requestDTO) {
		AuthorDTO createdAuthor = authorService.createAuthor(requestDTO);
		return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
	}

	/**
	 * UC-AUTHOR-002: Lấy danh sách tác giả
	 */
	@GetMapping
	public ResponseEntity<Page<AuthorDTO>> getAllAuthors(
		@PageableDefault(size = 20) Pageable pageable,
		@RequestParam(required = false) String search
	) {
		Page<AuthorDTO> authors = authorService.getAllAuthors(pageable, search);
		return ResponseEntity.ok(authors);
	}

	/**
	 * UC-AUTHOR-003: Lấy chi tiết tác giả
	 */
	@GetMapping("/{id}")
	public ResponseEntity<AuthorDetailDTO> getAuthorById(@PathVariable Long id) {
		AuthorDetailDTO author = authorService.getAuthorById(id);
		return ResponseEntity.ok(author);
	}

	/**
	 * UC-AUTHOR-004: Cập nhật thông tin tác giả
	 */
	@PutMapping("/{id}")
	public ResponseEntity<AuthorDTO> updateAuthor(
		@PathVariable Long id,
		@Valid @RequestBody AuthorUpdateDTO updateDTO
	) {
		AuthorDTO updatedAuthor = authorService.updateAuthor(id, updateDTO);
		return ResponseEntity.ok(updatedAuthor);
	}

	/**
	 * UC-AUTHOR-005: Xóa tác giả
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
		authorService.deleteAuthor(id);
		return ResponseEntity.noContent().build();
	}
}
