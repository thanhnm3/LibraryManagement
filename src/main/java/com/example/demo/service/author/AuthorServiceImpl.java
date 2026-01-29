package com.example.demo.service.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.author.AuthorDetailDTO;
import com.example.demo.dto.author.AuthorRequestDTO;
import com.example.demo.dto.author.AuthorUpdateDTO;
import com.example.demo.entity.Author;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for Author operations
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;
	private final AuthorMapper authorMapper;

	public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
		this.authorRepository = authorRepository;
		this.authorMapper = authorMapper;
	}

	/**
	 * Tạo tác giả mới
	 * 
	 * @param requestDTO - Thông tin tác giả
	 * @return AuthorDTO
	 */
	@Override
	public AuthorDTO createAuthor(AuthorRequestDTO requestDTO) {
		Author author = authorMapper.toEntity(requestDTO);
		Author savedAuthor = authorRepository.save(author);
		return authorMapper.toDTO(savedAuthor);
	}

	/**
	 * Lấy danh sách tác giả
	 * 
	 * @param pageable - Thông tin phân trang
	 * @param search   - Từ khóa tìm kiếm (optional)
	 * @return Page<AuthorDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<AuthorDTO> getAllAuthors(Pageable pageable, String search) {
		String searchTerm = (search != null && !search.trim().isEmpty()) ? search : null;
		Page<Author> authors = authorRepository.findAllWithSearch(searchTerm, pageable);
		return authors.map(authorMapper::toDTO);
	}

	/**
	 * Lấy chi tiết tác giả
	 * 
	 * @param authorId - ID tác giả
	 * @return AuthorDetailDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public AuthorDetailDTO getAuthorById(Long authorId) {
		Author author = authorRepository.findById(authorId)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

		return authorMapper.toDetailDTO(author);
	}

	/**
	 * Cập nhật thông tin tác giả
	 * 
	 * @param authorId - ID tác giả
	 * @param updateDTO - Thông tin cập nhật
	 * @return AuthorDTO
	 */
	@Override
	public AuthorDTO updateAuthor(Long authorId, AuthorUpdateDTO updateDTO) {
		Author author = authorRepository.findById(authorId)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

		authorMapper.updateEntityFromDTO(author, updateDTO);

		Author updatedAuthor = authorRepository.save(author);
		return authorMapper.toDTO(updatedAuthor);
	}

	/**
	 * Xóa tác giả
	 * 
	 * @param authorId - ID tác giả
	 */
	@Override
	public void deleteAuthor(Long authorId) {
		Author author = authorRepository.findById(authorId)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

		if (!author.getBooks().isEmpty()) {
			throw new BusinessRuleException(
					"Cannot delete author with id: " + authorId + " because it has associated books");
		}

		authorRepository.delete(author);
	}
}
