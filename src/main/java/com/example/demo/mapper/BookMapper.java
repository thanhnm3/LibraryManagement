package com.example.demo.mapper;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookDetailDTO;
import com.example.demo.dto.book.BookRequestDTO;
import com.example.demo.dto.book.BookUpdateDTO;
import com.example.demo.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper class for converting between Book entity and DTOs
 */
@Component
public class BookMapper {

	private final AuthorMapper authorMapper;
	private final PublisherMapper publisherMapper;
	private final CategoryMapper categoryMapper;

	public BookMapper(AuthorMapper authorMapper, PublisherMapper publisherMapper, CategoryMapper categoryMapper) {
		this.authorMapper = authorMapper;
		this.publisherMapper = publisherMapper;
		this.categoryMapper = categoryMapper;
	}

	/**
	 * Convert Book entity to BookDTO
	 * 
	 * @param book - Book entity
	 * @return BookDTO
	 */
	public BookDTO toDTO(Book book) {
		if (book == null) {
			return null;
		}

		BookDTO dto = new BookDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setIsbn(book.getIsbn());
		dto.setPublicationYear(book.getPublicationYear());
		dto.setDescription(book.getDescription());
		dto.setCoverImageUrl(book.getCoverImageUrl());
		dto.setFilePath(book.getFilePath());

		if (book.getPublisher() != null) {
			dto.setPublisher(publisherMapper.toDTO(book.getPublisher()));
		}

		if (book.getAuthors() != null) {
			dto.setAuthors(book.getAuthors().stream()
					.map(authorMapper::toDTO)
					.collect(Collectors.toList()));
		}

		if (book.getCategories() != null) {
			dto.setCategories(book.getCategories().stream()
					.map(categoryMapper::toDTO)
					.collect(Collectors.toList()));
		}

		return dto;
	}

	/**
	 * Convert Book entity to BookDetailDTO
	 * 
	 * @param book - Book entity
	 * @return BookDetailDTO
	 */
	public BookDetailDTO toDetailDTO(Book book) {
		if (book == null) {
			return null;
		}

		BookDetailDTO dto = new BookDetailDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setIsbn(book.getIsbn());
		dto.setPublicationYear(book.getPublicationYear());
		dto.setDescription(book.getDescription());
		dto.setCoverImageUrl(book.getCoverImageUrl());
		dto.setFilePath(book.getFilePath());

		if (book.getPublisher() != null) {
			dto.setPublisher(publisherMapper.toDTO(book.getPublisher()));
		}

		if (book.getAuthors() != null) {
			dto.setAuthors(book.getAuthors().stream()
					.map(authorMapper::toDTO)
					.collect(Collectors.toList()));
		}

		if (book.getCategories() != null) {
			dto.setCategories(book.getCategories().stream()
					.map(categoryMapper::toDTO)
					.collect(Collectors.toList()));
		}

		return dto;
	}

	/**
	 * Convert BookRequestDTO to Book entity (without relationships)
	 * Relationships should be set separately in service
	 * 
	 * @param requestDTO - BookRequestDTO
	 * @return Book entity
	 */
	public Book toEntity(BookRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		Book book = new Book();
		book.setTitle(requestDTO.getTitle());
		book.setIsbn(requestDTO.getIsbn());
		book.setPublicationYear(requestDTO.getPublicationYear());
		book.setDescription(requestDTO.getDescription());
		book.setCoverImageUrl(requestDTO.getCoverImageUrl());
		book.setFilePath(requestDTO.getFilePath());
		return book;
	}

	/**
	 * Update Book entity from BookUpdateDTO
	 * Relationships should be updated separately in service
	 * 
	 * @param book     - Book entity to update
	 * @param updateDTO - BookUpdateDTO with new values
	 */
	public void updateEntityFromDTO(Book book, BookUpdateDTO updateDTO) {
		if (book == null || updateDTO == null) {
			return;
		}

		if (updateDTO.getTitle() != null) {
			book.setTitle(updateDTO.getTitle());
		}

		if (updateDTO.getIsbn() != null) {
			book.setIsbn(updateDTO.getIsbn());
		}

		if (updateDTO.getPublicationYear() != null) {
			book.setPublicationYear(updateDTO.getPublicationYear());
		}

		if (updateDTO.getDescription() != null) {
			book.setDescription(updateDTO.getDescription());
		}

		if (updateDTO.getCoverImageUrl() != null) {
			book.setCoverImageUrl(updateDTO.getCoverImageUrl());
		}

		if (updateDTO.getFilePath() != null) {
			book.setFilePath(updateDTO.getFilePath());
		}
	}
}
