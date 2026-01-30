package com.example.demo.mapper;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.author.AuthorDetailDTO;
import com.example.demo.dto.author.AuthorRequestDTO;
import com.example.demo.dto.author.AuthorUpdateDTO;
import com.example.demo.entity.Author;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper class for converting between Author entity and DTOs
 */
@Component
public class AuthorMapper {

	private final BookMapper bookMapper;

	public AuthorMapper(@Lazy BookMapper bookMapper) {
		this.bookMapper = bookMapper;
	}

	/**
	 * Convert Author entity to AuthorDTO
	 * 
	 * @param author - Author entity
	 * @return AuthorDTO
	 */
	public AuthorDTO toDTO(Author author) {
		if (author == null) {
			return null;
		}

		AuthorDTO dto = new AuthorDTO();
		dto.setId(author.getId());
		dto.setFullName(author.getFullName());
		dto.setBio(author.getBio());
		return dto;
	}

	/**
	 * Convert Author entity to AuthorDetailDTO
	 * 
	 * @param author - Author entity
	 * @return AuthorDetailDTO
	 */
	public AuthorDetailDTO toDetailDTO(Author author) {
		if (author == null) {
			return null;
		}

		AuthorDetailDTO dto = new AuthorDetailDTO();
		dto.setId(author.getId());
		dto.setFullName(author.getFullName());
		dto.setBio(author.getBio());
		dto.setCreatedAt(author.getCreatedAt());
		dto.setBooks(author.getBooks().stream()
				.map(bookMapper::toDTO)
				.collect(Collectors.toList()));
		return dto;
	}

	/**
	 * Convert AuthorRequestDTO to Author entity
	 * 
	 * @param requestDTO - AuthorRequestDTO
	 * @return Author entity
	 */
	public Author toEntity(AuthorRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		Author author = new Author();
		author.setFullName(requestDTO.getFullName());
		author.setBio(requestDTO.getBio());
		return author;
	}

	/**
	 * Update Author entity from AuthorUpdateDTO
	 * 
	 * @param author    - Author entity to update
	 * @param updateDTO - AuthorUpdateDTO with new values
	 */
	public void updateEntityFromDTO(Author author, AuthorUpdateDTO updateDTO) {
		if (author == null || updateDTO == null) {
			return;
		}

		if (updateDTO.getFullName() != null) {
			author.setFullName(updateDTO.getFullName());
		}

		if (updateDTO.getBio() != null) {
			author.setBio(updateDTO.getBio());
		}
	}
}
