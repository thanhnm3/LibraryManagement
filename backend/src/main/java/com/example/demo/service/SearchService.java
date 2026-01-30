package com.example.demo.service;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookSearchCriteriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Search operations
 */
public interface SearchService {

	/**
	 * UC-SEARCH-001: Tìm kiếm nâng cao (Multi-table join)
	 * Query phức tạp nối 5 bảng (books, categories, authors, book_categories, book_authors, loans)
	 * 
	 * @param categoryName - Tên danh mục (optional)
	 * @param authorName   - Tên tác giả (optional, partial match)
	 * @param userId       - ID user (optional - sách đang được mượn bởi user)
	 * @param title        - Tiêu đề sách (optional, partial match)
	 * @return List<BookDTO> - Danh sách sách tìm được
	 */
	List<BookDTO> advancedSearch(String categoryName, String authorName, Long userId, String title);

	/**
	 * UC-SEARCH-002: Tìm kiếm sách theo nhiều tiêu chí
	 * 
	 * @param criteria - Tiêu chí tìm kiếm
	 * @param pageable - Pagination info
	 * @return Page<BookDTO> - Danh sách sách với pagination
	 */
	Page<BookDTO> searchBooks(BookSearchCriteriaDTO criteria, Pageable pageable);
}
