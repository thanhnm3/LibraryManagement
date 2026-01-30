package com.example.demo.util;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.author.AuthorRequestDTO;
import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.book.BookRequestDTO;
import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.dto.category.CategoryRequestDTO;
import com.example.demo.dto.publisher.PublisherDTO;
import com.example.demo.dto.publisher.PublisherRequestDTO;
import com.example.demo.dto.review.ReviewDTO;
import com.example.demo.dto.review.ReviewRequestDTO;
import com.example.demo.dto.loan.LoanDTO;
import com.example.demo.dto.loan.LoanDetailDTO;
import com.example.demo.dto.loan.LoanRequestDTO;
import com.example.demo.dto.loan.LoanRenewalRequestDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserRequestDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.entity.Loan;
import com.example.demo.entity.Publisher;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Helper class để tạo mock entities và DTOs cho testing
 */
public class TestDataBuilder {

  public static Author createAuthor() {
    Author author = new Author();
    author.setId(1L);
    author.setFullName("Test Author");
    author.setBio("Test Bio");
    author.setCreatedAt(LocalDateTime.now());
    author.setBooks(new ArrayList<>());
    return author;
  }

  public static AuthorDTO createAuthorDTO() {
    AuthorDTO dto = new AuthorDTO();
    dto.setId(1L);
    dto.setFullName("Test Author");
    dto.setBio("Test Bio");
    return dto;
  }

  public static AuthorRequestDTO createAuthorRequestDTO() {
    AuthorRequestDTO dto = new AuthorRequestDTO();
    dto.setFullName("Test Author");
    dto.setBio("Test Bio");
    return dto;
  }

  public static com.example.demo.dto.author.AuthorDetailDTO createAuthorDetailDTO() {
    com.example.demo.dto.author.AuthorDetailDTO dto = new com.example.demo.dto.author.AuthorDetailDTO();
    dto.setId(1L);
    dto.setFullName("Test Author");
    dto.setBio("Test Bio");
    return dto;
  }

  public static com.example.demo.dto.author.AuthorUpdateDTO createAuthorUpdateDTO() {
    com.example.demo.dto.author.AuthorUpdateDTO dto = new com.example.demo.dto.author.AuthorUpdateDTO();
    dto.setFullName("Updated Author");
    dto.setBio("Updated Bio");
    return dto;
  }

  public static Book createBook() {
    Book book = new Book();
    book.setId(1L);
    book.setTitle("Test Book");
    book.setIsbn("1234567890");
    book.setPublicationYear(2020);
    book.setDescription("Test Description");
    book.setAuthors(new ArrayList<>());
    book.setCategories(new ArrayList<>());
    return book;
  }

  /**
   * Tạo Book với id, title và isbn tùy chỉnh (dùng cho BookServiceImplTest, v.v.)
   */
  public static Book createBook(Long id, String title, String isbn) {
    Book book = createBook();
    book.setId(id);
    book.setTitle(title);
    book.setIsbn(isbn);
    return book;
  }

  public static BookDTO createBookDTO() {
    BookDTO dto = new BookDTO();
    dto.setId(1L);
    dto.setTitle("Test Book");
    dto.setIsbn("1234567890");
    dto.setPublicationYear(2020);
    return dto;
  }

  public static BookRequestDTO createBookRequestDTO() {
    BookRequestDTO dto = new BookRequestDTO();
    dto.setTitle("Test Book");
    dto.setIsbn("1234567890");
    dto.setPublicationYear(2020);
    dto.setDescription("Test Description");
    return dto;
  }

  public static com.example.demo.dto.book.BookDetailDTO createBookDetailDTO() {
    com.example.demo.dto.book.BookDetailDTO dto = new com.example.demo.dto.book.BookDetailDTO();
    dto.setId(1L);
    dto.setTitle("Test Book");
    dto.setIsbn("1234567890");
    dto.setPublicationYear(2020);
    dto.setDescription("Test Description");
    dto.setPublisher(createPublisherDTO());
    dto.setAuthors(new ArrayList<>());
    dto.setCategories(new ArrayList<>());
    return dto;
  }

  public static com.example.demo.dto.book.BookUpdateDTO createBookUpdateDTO() {
    com.example.demo.dto.book.BookUpdateDTO dto = new com.example.demo.dto.book.BookUpdateDTO();
    dto.setTitle("Updated Book");
    dto.setIsbn("9876543210");
    dto.setPublicationYear(2021);
    dto.setDescription("Updated Description");
    return dto;
  }

  public static User createUser() {
    User user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setPasswordHash("hashedPassword");
    user.setFullName("Test User");
    user.setStatus(UserStatus.ACTIVE);
    user.setRole(UserRole.MEMBER);
    user.setCreatedAt(LocalDateTime.now());
    user.setLoans(new ArrayList<>());
    user.setReviews(new ArrayList<>());
    return user;
  }

  public static User createUserWithStatus(UserStatus status) {
    User user = createUser();
    user.setStatus(status);
    return user;
  }

  /**
   * Tạo User với id, email và status tùy chỉnh (dùng cho LoanServiceImplTest, v.v.)
   */
  public static User createUser(Long id, String email, UserStatus status) {
    User user = createUser();
    user.setId(id);
    user.setEmail(email);
    user.setStatus(status);
    return user;
  }

  public static UserDTO createUserDTO() {
    UserDTO dto = new UserDTO();
    dto.setId(1L);
    dto.setEmail("test@example.com");
    dto.setFullName("Test User");
    dto.setStatus(UserStatus.ACTIVE);
    dto.setRole(UserRole.MEMBER);
    dto.setCreatedAt(LocalDateTime.now());
    return dto;
  }

  public static UserRequestDTO createUserRequestDTO() {
    UserRequestDTO dto = new UserRequestDTO();
    dto.setEmail("test@example.com");
    dto.setPassword("password123");
    dto.setFullName("Test User");
    return dto;
  }

  public static Review createReview() {
    Review review = new Review();
    review.setId(1L);
    review.setUser(createUser());
    review.setBook(createBook());
    review.setRating(5);
    review.setComment("Great book!");
    review.setCreatedAt(LocalDateTime.now());
    return review;
  }

  public static Review createReviewWithUserId(Long userId) {
    Review review = createReview();
    User user = createUser();
    user.setId(userId);
    review.setUser(user);
    return review;
  }

  public static ReviewDTO createReviewDTO() {
    ReviewDTO dto = new ReviewDTO();
    dto.setId(1L);
    dto.setUserId(1L);
    dto.setUserFullName("Test User");
    dto.setBookId(1L);
    dto.setBookTitle("Test Book");
    dto.setRating(5);
    dto.setComment("Great book!");
    dto.setCreatedAt(LocalDateTime.now());
    return dto;
  }

  public static ReviewRequestDTO createReviewRequestDTO() {
    ReviewRequestDTO dto = new ReviewRequestDTO();
    dto.setUserId(1L);
    dto.setBookId(1L);
    dto.setRating(5);
    dto.setComment("Great book!");
    return dto;
  }

  public static Publisher createPublisher() {
    Publisher publisher = new Publisher();
    publisher.setId(1L);
    publisher.setName("Test Publisher");
    publisher.setWebsite("https://test.com");
    publisher.setAddress("Test Address");
    publisher.setCreatedAt(LocalDateTime.now());
    publisher.setBooks(new ArrayList<>());
    return publisher;
  }

  /**
   * Tạo Publisher với id và name tùy chỉnh (dùng cho BookServiceImplTest, v.v.)
   */
  public static Publisher createPublisher(Long id, String name) {
    Publisher publisher = createPublisher();
    publisher.setId(id);
    publisher.setName(name);
    return publisher;
  }

  public static PublisherDTO createPublisherDTO() {
    PublisherDTO dto = new PublisherDTO();
    dto.setId(1L);
    dto.setName("Test Publisher");
    dto.setWebsite("https://test.com");
    dto.setAddress("Test Address");
    return dto;
  }

  public static PublisherRequestDTO createPublisherRequestDTO() {
    PublisherRequestDTO dto = new PublisherRequestDTO();
    dto.setName("Test Publisher");
    dto.setWebsite("https://test.com");
    dto.setAddress("Test Address");
    return dto;
  }

  public static Category createCategory() {
    Category category = new Category();
    category.setId(1L);
    category.setName("Test Category");
    category.setDescription("Test Description");
    category.setBooks(new ArrayList<>());
    return category;
  }

  public static CategoryDTO createCategoryDTO() {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(1L);
    dto.setName("Test Category");
    dto.setDescription("Test Description");
    return dto;
  }

  public static CategoryRequestDTO createCategoryRequestDTO() {
    CategoryRequestDTO dto = new CategoryRequestDTO();
    dto.setName("Test Category");
    dto.setDescription("Test Description");
    return dto;
  }

  public static com.example.demo.dto.category.CategoryDetailDTO createCategoryDetailDTO() {
    com.example.demo.dto.category.CategoryDetailDTO dto = new com.example.demo.dto.category.CategoryDetailDTO();
    dto.setId(1L);
    dto.setName("Test Category");
    dto.setDescription("Test Description");
    return dto;
  }

  public static com.example.demo.dto.category.CategoryUpdateDTO createCategoryUpdateDTO() {
    com.example.demo.dto.category.CategoryUpdateDTO dto = new com.example.demo.dto.category.CategoryUpdateDTO();
    dto.setName("Updated Category");
    dto.setDescription("Updated Description");
    return dto;
  }

  public static com.example.demo.dto.publisher.PublisherDetailDTO createPublisherDetailDTO() {
    com.example.demo.dto.publisher.PublisherDetailDTO dto = new com.example.demo.dto.publisher.PublisherDetailDTO();
    dto.setId(1L);
    dto.setName("Test Publisher");
    dto.setWebsite("https://test.com");
    dto.setAddress("Test Address");
    return dto;
  }

  public static com.example.demo.dto.publisher.PublisherUpdateDTO createPublisherUpdateDTO() {
    com.example.demo.dto.publisher.PublisherUpdateDTO dto = new com.example.demo.dto.publisher.PublisherUpdateDTO();
    dto.setName("Updated Publisher");
    dto.setWebsite("https://updated.com");
    dto.setAddress("Updated Address");
    return dto;
  }

  public static Loan createLoan() {
    Loan loan = new Loan();
    loan.setId(1L);
    loan.setUser(createUser());
    loan.setBook(createBook());
    loan.setBorrowDate(LocalDateTime.now());
    loan.setDueDate(LocalDateTime.now().plusDays(14));
    loan.setStatus(LoanStatus.BORROWED);
    return loan;
  }

  public static Loan createLoan(Long id, LoanStatus status) {
    Loan loan = createLoan();
    loan.setId(id);
    loan.setStatus(status);
    return loan;
  }

  public static LoanDTO createLoanDTO() {
    LoanDTO dto = new LoanDTO();
    dto.setId(1L);
    dto.setUserId(1L);
    dto.setUserFullName("Test User");
    dto.setBookId(1L);
    dto.setBookTitle("Test Book");
    dto.setBorrowDate(LocalDateTime.now());
    dto.setDueDate(LocalDateTime.now().plusDays(14));
    dto.setStatus(LoanStatus.BORROWED);
    return dto;
  }

  public static LoanDetailDTO createLoanDetailDTO() {
    LoanDetailDTO dto = new LoanDetailDTO();
    dto.setId(1L);
    dto.setUser(createUserDTO());
    dto.setBook(createBookDTO());
    dto.setBorrowDate(LocalDateTime.now());
    dto.setDueDate(LocalDateTime.now().plusDays(14));
    dto.setStatus(LoanStatus.BORROWED);
    return dto;
  }

  public static LoanRequestDTO createLoanRequestDTO() {
    LoanRequestDTO dto = new LoanRequestDTO();
    dto.setUserId(1L);
    dto.setBookId(1L);
    dto.setDueDate(LocalDateTime.now().plusDays(14));
    return dto;
  }

  public static LoanRenewalRequestDTO createLoanRenewalRequestDTO() {
    LoanRenewalRequestDTO dto = new LoanRenewalRequestDTO();
    dto.setNewDueDate(LocalDateTime.now().plusDays(21));
    return dto;
  }
}
