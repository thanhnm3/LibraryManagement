package com.example.demo.service.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookDetailDTO;
import com.example.demo.dto.book.BookRequestDTO;
import com.example.demo.dto.book.BookUpdateDTO;
import com.example.demo.dto.review.ReviewSummaryDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.entity.Publisher;
import com.example.demo.enums.LoanStatus;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.PublisherRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for Book operations
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final PublisherRepository publisherRepository;
	private final AuthorRepository authorRepository;
	private final CategoryRepository categoryRepository;
	private final LoanRepository loanRepository;
	private final ReviewRepository reviewRepository;
	private final BookMapper bookMapper;

	public BookServiceImpl(
			BookRepository bookRepository,
			PublisherRepository publisherRepository,
			AuthorRepository authorRepository,
			CategoryRepository categoryRepository,
			LoanRepository loanRepository,
			ReviewRepository reviewRepository,
			BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.publisherRepository = publisherRepository;
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
		this.loanRepository = loanRepository;
		this.reviewRepository = reviewRepository;
		this.bookMapper = bookMapper;
	}

	/**
	 * Tạo sách mới
	 * 
	 * @param requestDTO - Thông tin sách
	 * @return BookDTO
	 */
	@Override
	public BookDTO createBook(BookRequestDTO requestDTO) {
		if (bookRepository.findByIsbn(requestDTO.getIsbn()).isPresent()) {
			throw new DuplicateResourceException("Book with ISBN '" + requestDTO.getIsbn() + "' already exists");
		}

		Publisher publisher = publisherRepository.findById(requestDTO.getPublisherId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Publisher not found with id: " + requestDTO.getPublisherId()));

		Book book = bookMapper.toEntity(requestDTO);
		book.setPublisher(publisher);

		if (requestDTO.getAuthorIds() != null && !requestDTO.getAuthorIds().isEmpty()) {
			List<Author> authors = authorRepository.findAllById(requestDTO.getAuthorIds());
			if (authors.size() != requestDTO.getAuthorIds().size()) {
				throw new ResourceNotFoundException("One or more authors not found");
			}
			book.setAuthors(authors);
		}

		if (requestDTO.getCategoryIds() != null && !requestDTO.getCategoryIds().isEmpty()) {
			List<Category> categories = categoryRepository.findAllById(requestDTO.getCategoryIds());
			if (categories.size() != requestDTO.getCategoryIds().size()) {
				throw new ResourceNotFoundException("One or more categories not found");
			}
			book.setCategories(categories);
		}

		Book savedBook = bookRepository.save(book);
		return bookMapper.toDTO(savedBook);
	}

	/**
	 * Lấy danh sách sách
	 * 
	 * @param pageable - Thông tin phân trang
	 * @return Page<BookDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<BookDTO> getAllBooks(Pageable pageable) {
		return bookRepository.findAll(pageable)
				.map(bookMapper::toDTO);
	}

	/**
	 * Lấy chi tiết sách theo ID
	 * 
	 * @param bookId - ID sách
	 * @return BookDetailDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public BookDetailDTO getBookById(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

		BookDetailDTO dto = bookMapper.toDetailDTO(book);

		List<com.example.demo.entity.Review> reviews = reviewRepository.findListByBookId(bookId);
		if (!reviews.isEmpty()) {
			double averageRating = reviews.stream()
					.mapToInt(com.example.demo.entity.Review::getRating)
					.average()
					.orElse(0.0);
			ReviewSummaryDTO reviewSummary = new ReviewSummaryDTO();
			reviewSummary.setAverageRating(averageRating);
			reviewSummary.setTotalReviews((long) reviews.size());
			dto.setReviewSummary(reviewSummary);
		}

		return dto;
	}

	/**
	 * Cập nhật thông tin sách
	 * 
	 * @param bookId    - ID sách
	 * @param updateDTO - Thông tin cập nhật
	 * @return BookDTO
	 */
	@Override
	public BookDTO updateBook(Long bookId, BookUpdateDTO updateDTO) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

		if (updateDTO.getIsbn() != null && !updateDTO.getIsbn().equals(book.getIsbn())) {
			if (bookRepository.findByIsbn(updateDTO.getIsbn()).isPresent()) {
				throw new DuplicateResourceException("Book with ISBN '" + updateDTO.getIsbn() + "' already exists");
			}
		}

		bookMapper.updateEntityFromDTO(book, updateDTO);

		if (updateDTO.getPublisherId() != null) {
			Publisher publisher = publisherRepository.findById(updateDTO.getPublisherId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Publisher not found with id: " + updateDTO.getPublisherId()));
			book.setPublisher(publisher);
		}

		if (updateDTO.getAuthorIds() != null) {
			List<Author> authors = new ArrayList<>();
			if (!updateDTO.getAuthorIds().isEmpty()) {
				authors = authorRepository.findAllById(updateDTO.getAuthorIds());
				if (authors.size() != updateDTO.getAuthorIds().size()) {
					throw new ResourceNotFoundException("One or more authors not found");
				}
			}
			book.setAuthors(authors);
		}

		if (updateDTO.getCategoryIds() != null) {
			List<Category> categories = new ArrayList<>();
			if (!updateDTO.getCategoryIds().isEmpty()) {
				categories = categoryRepository.findAllById(updateDTO.getCategoryIds());
				if (categories.size() != updateDTO.getCategoryIds().size()) {
					throw new ResourceNotFoundException("One or more categories not found");
				}
			}
			book.setCategories(categories);
		}

		Book updatedBook = bookRepository.save(book);
		return bookMapper.toDTO(updatedBook);
	}

	/**
	 * Xóa sách
	 * 
	 * @param bookId - ID sách
	 */
	@Override
	public void deleteBook(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

		List<com.example.demo.entity.Loan> activeLoans = loanRepository.findListByBookIdAndStatus(bookId,
				LoanStatus.BORROWED);
		if (!activeLoans.isEmpty()) {
			throw new BusinessRuleException("Cannot delete book with id: " + bookId + " because it has active loans");
		}

		bookRepository.delete(book);
	}
}
