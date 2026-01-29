package com.example.demo.controller;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookDetailDTO;
import com.example.demo.dto.book.BookRequestDTO;
import com.example.demo.dto.book.BookUpdateDTO;
import com.example.demo.service.book.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	/**
	 * UC-BOOK-001: Tạo sách mới
	 */
	@PostMapping
	public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookRequestDTO requestDTO) {
		BookDTO createdBook = bookService.createBook(requestDTO);
		return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
	}

	/**
	 * UC-BOOK-002: Lấy danh sách sách
	 */
	@GetMapping
	public ResponseEntity<Page<BookDTO>> getAllBooks(
		@PageableDefault(size = 20) Pageable pageable
	) {
		Page<BookDTO> books = bookService.getAllBooks(pageable);
		return ResponseEntity.ok(books);
	}

	/**
	 * UC-BOOK-003: Lấy chi tiết sách theo ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<BookDetailDTO> getBookById(@PathVariable Long id) {
		BookDetailDTO book = bookService.getBookById(id);
		return ResponseEntity.ok(book);
	}

	/**
	 * UC-BOOK-004: Cập nhật thông tin sách
	 */
	@PutMapping("/{id}")
	public ResponseEntity<BookDTO> updateBook(
		@PathVariable Long id,
		@Valid @RequestBody BookUpdateDTO updateDTO
	) {
		BookDTO updatedBook = bookService.updateBook(id, updateDTO);
		return ResponseEntity.ok(updatedBook);
	}

	/**
	 * UC-BOOK-005: Xóa sách
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
}
