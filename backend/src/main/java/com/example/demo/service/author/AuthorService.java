package com.example.demo.service.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.author.AuthorDetailDTO;
import com.example.demo.dto.author.AuthorRequestDTO;
import com.example.demo.dto.author.AuthorUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Author operations
 */
public interface AuthorService {

	/**
	 * Tạo tác giả mới
	 * 
	 * @param requestDTO - Thông tin tác giả
	 * @return AuthorDTO
	 */
	AuthorDTO createAuthor(AuthorRequestDTO requestDTO);

	/**
	 * Lấy danh sách tác giả
	 * 
	 * @param pageable - Thông tin phân trang
	 * @param search   - Từ khóa tìm kiếm (optional)
	 * @return Page<AuthorDTO>
	 */
	Page<AuthorDTO> getAllAuthors(Pageable pageable, String search);

	/**
	 * Lấy chi tiết tác giả
	 * 
	 * @param authorId - ID tác giả
	 * @return AuthorDetailDTO
	 */
	AuthorDetailDTO getAuthorById(Long authorId);

	/**
	 * Cập nhật thông tin tác giả
	 * 
	 * @param authorId  - ID tác giả
	 * @param updateDTO - Thông tin cập nhật
	 * @return AuthorDTO
	 */
	AuthorDTO updateAuthor(Long authorId, AuthorUpdateDTO updateDTO);

	/**
	 * Xóa tác giả
	 * 
	 * @param authorId - ID tác giả
	 */
	void deleteAuthor(Long authorId);
}
