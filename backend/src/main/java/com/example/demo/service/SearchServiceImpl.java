package com.example.demo.service;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookSearchCriteriaDTO;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Search operations (UC-SEARCH-001, UC-SEARCH-002)
 */
@Service
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;

	public SearchServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.bookMapper = bookMapper;
	}

	@Override
	public List<BookDTO> advancedSearch(String categoryName, String authorName, Long userId, String title) {
		List<Book> books = bookRepository.advancedSearch(categoryName, authorName, userId, title);
		return books.stream().map(bookMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Page<BookDTO> searchBooks(BookSearchCriteriaDTO criteria, Pageable pageable) {
		return bookRepository.searchBooks(
				criteria.getTitle(),
				criteria.getAuthor(),
				criteria.getCategory(),
				criteria.getPublisher(),
				criteria.getIsbn(),
				criteria.getMinYear(),
				criteria.getMaxYear(),
				pageable
		).map(bookMapper::toDTO);
	}
}
