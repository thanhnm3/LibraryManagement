package com.example.demo.service.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookDetailDTO;
import com.example.demo.dto.book.BookRequestDTO;
import com.example.demo.dto.book.BookUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Book operations
 */
public interface BookService {

	/**
	 * Tạo sách mới
	 * 
	 * @param requestDTO - Thông tin sách
	 * @return BookDTO
	 */
	BookDTO createBook(BookRequestDTO requestDTO);

	/**
	 * Lấy danh sách sách
	 * 
	 * @param pageable - Thông tin phân trang
	 * @return Page<BookDTO>
	 */
	Page<BookDTO> getAllBooks(Pageable pageable);

	/**
	 * Lấy chi tiết sách theo ID
	 * 
	 * @param bookId - ID sách
	 * @return BookDetailDTO
	 */
	BookDetailDTO getBookById(Long bookId);

	/**
	 * Cập nhật thông tin sách
	 * 
	 * @param bookId    - ID sách
	 * @param updateDTO - Thông tin cập nhật
	 * @return BookDTO
	 */
	BookDTO updateBook(Long bookId, BookUpdateDTO updateDTO);

	/**
	 * Xóa sách
	 * 
	 * @param bookId - ID sách
	 */
	void deleteBook(Long bookId);
}
