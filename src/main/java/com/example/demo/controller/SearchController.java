package com.example.demo.controller;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookSearchCriteriaDTO;
import com.example.demo.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	/**
	 * UC-SEARCH-001: Tìm kiếm nâng cao (Multi-table join)
	 */
	@GetMapping("/advanced")
	public ResponseEntity<List<BookDTO>> advancedSearch(
			@RequestParam(required = false) String categoryName,
			@RequestParam(required = false) String authorName,
			@RequestParam(required = false) Long userId,
			@RequestParam(required = false) String title) {
		List<BookDTO> books = searchService.advancedSearch(categoryName, authorName, userId, title);
		return ResponseEntity.ok(books);
	}

	/**
	 * UC-SEARCH-002: Tìm kiếm sách theo nhiều tiêu chí
	 */
	@PostMapping("/books")
	public ResponseEntity<Page<BookDTO>> searchBooks(
			@RequestBody BookSearchCriteriaDTO criteria,
			@PageableDefault(size = 20) Pageable pageable) {
		Page<BookDTO> books = searchService.searchBooks(criteria, pageable);
		return ResponseEntity.ok(books);
	}
}
