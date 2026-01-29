# Library Management System

Hệ thống quản lý thư viện được xây dựng bằng Spring Boot, cung cấp các chức năng quản lý sách, tác giả, người dùng, mượn trả sách, đánh giá và báo cáo.

## 📋 Mục lục

- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc Project](#cấu-trúc-project)
- [Design Patterns đã implement](#design-patterns-đã-implement)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Hướng dẫn khởi động](#hướng-dẫn-khởi-động)
- [Cấu trúc Database](#cấu-trúc-database)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Cấu trúc Code](#cấu-trúc-code)

## 🛠 Công nghệ sử dụng

### Backend Framework
- **Spring Boot 4.0.2** - Framework chính
- **Java 17** - Ngôn ngữ lập trình
- **Spring Data JPA** - ORM và quản lý database
- **Spring Web MVC** - RESTful API
- **Spring Validation** - Validation dữ liệu
- **Spring Security Crypto** - Mã hóa mật khẩu

### Database
- **PostgreSQL 16** - Database chính
- **Hibernate** - JPA Implementation

### Build Tool
- **Gradle** - Build automation tool
- **Lombok** - Giảm boilerplate code

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing

### Containerization
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## 📁 Cấu trúc Project

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business logic layer
│   │   │   │   ├── author/
│   │   │   │   ├── book/
│   │   │   │   ├── category/
│   │   │   │   ├── publisher/
│   │   │   │   ├── review/
│   │   │   │   └── user/
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── mapper/              # Entity-DTO mapping
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── enums/               # Enum classes
│   │   │   └── exception/           # Custom exceptions
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/demo/
│           ├── service/             # Service tests
│           └── util/                # Test utilities
├── docker/
│   └── init/                        # Database initialization scripts
├── build.gradle                     # Build configuration
├── docker-compose.yml               # Docker Compose configuration
└── README.md
```

## 🎨 Design Patterns đã implement

### 1. **Layered Architecture Pattern**
Project được tổ chức theo kiến trúc phân lớp rõ ràng:

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database
```

**Ưu điểm:**
- Tách biệt rõ ràng các concerns
- Dễ dàng maintain và test
- Tuân thủ Single Responsibility Principle

### 2. **Repository Pattern**
Sử dụng Spring Data JPA Repository để abstract hóa data access layer:

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findAllWithSearch(String search, Pageable pageable);
}
```

**Ưu điểm:**
- Giảm boilerplate code
- Dễ dàng thay đổi data source
- Tập trung logic query

### 3. **Service Layer Pattern**
Mỗi domain có Service interface và Implementation:

```java
public interface AuthorService {
    AuthorDTO createAuthor(AuthorRequestDTO requestDTO);
    Page<AuthorDTO> getAllAuthors(Pageable pageable, String search);
    // ...
}

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    // Implementation
}
```

**Ưu điểm:**
- Tách biệt business logic khỏi controller
- Dễ dàng mock cho testing
- Có thể có nhiều implementation

### 4. **DTO Pattern (Data Transfer Object)**
Sử dụng DTO để tách biệt Entity và API contract:

- **RequestDTO**: Dữ liệu nhận từ client (AuthorRequestDTO, BookRequestDTO...)
- **ResponseDTO**: Dữ liệu trả về cho client (AuthorDTO, BookDTO...)
- **DetailDTO**: Dữ liệu chi tiết (AuthorDetailDTO, BookDetailDTO...)
- **UpdateDTO**: Dữ liệu cập nhật (AuthorUpdateDTO, BookUpdateDTO...)

**Ưu điểm:**
- Bảo vệ Entity khỏi thay đổi không mong muốn
- Tối ưu dữ liệu truyền qua network
- Versioning API dễ dàng hơn

### 5. **Mapper Pattern**
Sử dụng Mapper để chuyển đổi giữa Entity và DTO:

```java
@Component
public class AuthorMapper {
    public AuthorDTO toDTO(Author author) { ... }
    public Author toEntity(AuthorRequestDTO dto) { ... }
    public void updateEntityFromDTO(Author entity, AuthorUpdateDTO dto) { ... }
}
```

**Ưu điểm:**
- Tập trung logic mapping
- Dễ dàng maintain
- Tái sử dụng được

### 6. **Builder Pattern**
Sử dụng Lombok @Builder cho Entities:

```java
@Entity
@Builder
@Getter
@Setter
public class Author {
    // ...
}
```

**Ưu điểm:**
- Tạo object linh hoạt
- Code dễ đọc hơn
- Immutable objects

### 7. **Dependency Injection Pattern**
Sử dụng Constructor Injection:

```java
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(
            AuthorRepository authorRepository,
            AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }
}
```

**Ưu điểm:**
- Dễ dàng test (có thể mock dependencies)
- Tuân thủ Dependency Inversion Principle
- Immutable dependencies

### 8. **Exception Handling Pattern**
Sử dụng Global Exception Handler:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(...) { ... }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(...) { ... }
}
```

**Ưu điểm:**
- Xử lý exception tập trung
- Response format nhất quán
- Giảm code duplicate

### 9. **Factory Pattern (Test Utilities)**
Sử dụng TestDataBuilder để tạo test data:

```java
public class TestDataBuilder {
    public static Author createAuthor() { ... }
    public static AuthorDTO createAuthorDTO() { ... }
    // ...
}
```

**Ưu điểm:**
- Tạo test data dễ dàng
- Tái sử dụng code
- Dễ maintain

### 10. **Strategy Pattern (implicit)**
Sử dụng trong Service layer với các strategy khác nhau cho từng use case:

- Search strategies (advanced search, simple search)
- Filter strategies (by status, by user, by book)
- Report strategies (loan report, review report)

## 💻 Yêu cầu hệ thống

- **Java**: JDK 17 hoặc cao hơn
- **Gradle**: 8.0+ (hoặc sử dụng Gradle Wrapper)
- **Docker**: 20.10+ (để chạy PostgreSQL)
- **Docker Compose**: 2.0+ (để orchestrate containers)
- **PostgreSQL**: 16+ (hoặc sử dụng Docker)

## 🚀 Hướng dẫn khởi động

### Bước 1: Clone repository

```bash
git clone <repository-url>
cd demo
```

### Bước 2: Khởi động PostgreSQL bằng Docker Compose

```bash
docker-compose up -d
```

Lệnh này sẽ:
- Tạo PostgreSQL container
- Tự động chạy scripts trong `docker/init/` để tạo database schema
- Load seed data (nếu có)

Kiểm tra container đang chạy:
```bash
docker ps
```

### Bước 3: Cấu hình Database (nếu cần)

File `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/demo_db
spring.datasource.username=demo_user
spring.datasource.password=demo_password
```

### Bước 4: Build project

```bash
# Windows
gradlew.bat build

# Linux/Mac
./gradlew build
```

### Bước 5: Chạy ứng dụng

```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

Hoặc chạy trực tiếp:
```bash
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Bước 6: Kiểm tra ứng dụng

Ứng dụng sẽ chạy tại: `http://localhost:8080`

Kiểm tra health:
```bash
curl http://localhost:8080/api/authors
```

## 🗄 Cấu trúc Database

### Entities chính:

1. **Authors** - Quản lý tác giả
2. **Books** - Quản lý sách
3. **Publishers** - Quản lý nhà xuất bản
4. **Categories** - Quản lý danh mục
5. **Users** - Quản lý người dùng
6. **Loans** - Quản lý mượn trả sách
7. **Reviews** - Quản lý đánh giá sách

### Relationships:

- **Book ↔ Author**: Many-to-Many (book_authors)
- **Book ↔ Category**: Many-to-Many (book_categories)
- **Book ↔ Publisher**: Many-to-One
- **User ↔ Loan**: One-to-Many
- **Book ↔ Loan**: One-to-Many
- **User ↔ Review**: One-to-Many
- **Book ↔ Review**: One-to-Many

### Database Schema được tạo tự động từ:
- `docker/init/initial.sql` - Tạo tables và constraints
- `docker/init/seed_data.sql` - Seed data (nếu có)

## 📡 API Endpoints

### Authors API
- `POST /api/authors` - Tạo tác giả mới
- `GET /api/authors` - Lấy danh sách tác giả (có pagination và search)
- `GET /api/authors/{id}` - Lấy chi tiết tác giả
- `PUT /api/authors/{id}` - Cập nhật tác giả
- `DELETE /api/authors/{id}` - Xóa tác giả

### Books API
- `POST /api/books` - Tạo sách mới
- `GET /api/books` - Lấy danh sách sách
- `GET /api/books/{id}` - Lấy chi tiết sách
- `PUT /api/books/{id}` - Cập nhật sách
- `DELETE /api/books/{id}` - Xóa sách

### Loans API
- `POST /api/loans` - Mượn sách
- `POST /api/loans/{id}/return` - Trả sách
- `POST /api/loans/{id}/renew` - Gia hạn mượn sách
- `GET /api/loans` - Lấy danh sách mượn trả (có filters)
- `GET /api/loans/{id}` - Lấy chi tiết mượn trả
- `GET /api/loans/user/{userId}/history` - Lịch sử mượn của user
- `GET /api/loans/user/{userId}/active` - Sách đang mượn của user
- `GET /api/loans/overdue` - Sách quá hạn

### Users API
- `POST /api/users/register` - Đăng ký user mới
- `GET /api/users` - Lấy danh sách users
- `GET /api/users/{id}` - Lấy chi tiết user
- `PUT /api/users/{id}` - Cập nhật user
- `POST /api/users/{id}/change-password` - Đổi mật khẩu
- `PUT /api/users/{id}/status` - Cập nhật trạng thái user
- `PUT /api/users/{id}/role` - Cập nhật role user

### Reviews API
- `POST /api/reviews` - Tạo đánh giá
- `PUT /api/reviews/{id}` - Cập nhật đánh giá
- `DELETE /api/reviews/{id}` - Xóa đánh giá
- `GET /api/reviews/book/{bookId}` - Đánh giá theo sách
- `GET /api/reviews/user/{userId}` - Đánh giá theo user

### Search API
- `GET /api/search/advanced` - Tìm kiếm nâng cao
- `GET /api/search/books` - Tìm kiếm sách

### Reports API
- `GET /api/reports/dashboard` - Thống kê dashboard
- `GET /api/reports/loans` - Báo cáo mượn trả
- `GET /api/reports/reviews` - Báo cáo đánh giá

## 🧪 Testing

### Chạy tất cả tests

```bash
# Windows
gradlew.bat test

# Linux/Mac
./gradlew test
```

### Chạy test với coverage

```bash
gradlew test jacocoTestReport
```

### Test Structure

```
src/test/java/com/example/demo/
├── service/
│   ├── author/AuthorServiceImplTest.java
│   ├── book/BookServiceImplTest.java
│   ├── loan/LoanServiceImplTest.java
│   └── ...
└── util/
    └── TestDataBuilder.java
```

### Test Patterns

- **Unit Tests**: Sử dụng JUnit 5 + Mockito
- **Mock Strategy**: Mock tất cả dependencies (Repository, Mapper)
- **Test Naming**: `should[ExpectedBehavior]_When[StateUnderTest]`
- **AAA Pattern**: Arrange-Act-Assert

### Ví dụ Test:

```java
@Test
@DisplayName("Should create author when valid request")
void shouldCreateAuthor_WhenValidRequest() {
    // Arrange
    when(authorMapper.toEntity(requestDTO)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(savedAuthor);
    
    // Act
    AuthorDTO result = authorService.createAuthor(requestDTO);
    
    // Assert
    assertNotNull(result);
    verify(authorRepository, times(1)).save(author);
}
```

## 📝 Cấu trúc Code

### Naming Conventions

- **Classes**: PascalCase (AuthorService, BookController)
- **Methods**: camelCase (createAuthor, getAllBooks)
- **Variables**: camelCase (authorId, bookTitle)
- **Constants**: UPPER_SNAKE_CASE (MAX_RETRY_COUNT)
- **Packages**: lowercase (com.example.demo.service)

### Code Organization

1. **Controller**: Xử lý HTTP requests/responses
2. **Service**: Business logic
3. **Repository**: Data access
4. **Mapper**: Entity-DTO conversion
5. **DTO**: Data transfer objects
6. **Entity**: JPA entities
7. **Exception**: Custom exceptions

### Best Practices

- ✅ Sử dụng `@Transactional` cho service methods
- ✅ Sử dụng `@Transactional(readOnly = true)` cho read operations
- ✅ Constructor injection thay vì field injection
- ✅ Validation với `@Valid` annotation
- ✅ Custom exceptions cho business errors
- ✅ Global exception handler
- ✅ DTO pattern để tách biệt layers
- ✅ Mapper pattern để convert Entity ↔ DTO
- ✅ JSDoc comments cho public methods

## 🔧 Configuration

### Application Properties

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5433/demo_db
spring.datasource.username=demo_user
spring.datasource.password=demo_password

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Docker Compose

```yaml
services:
  postgres:
    image: postgres:16-alpine
    ports:
      - "5433:5432"
    environment:
      POSTGRES_DB: demo_db
      POSTGRES_USER: demo_user
      POSTGRES_PASSWORD: demo_password
```

## 📚 Use Cases đã implement

### Author Management (UC-AUTHOR-001 ~ 005)
- Tạo, đọc, cập nhật, xóa tác giả
- Tìm kiếm tác giả

### Book Management (UC-BOOK-001 ~ 005)
- Tạo, đọc, cập nhật, xóa sách
- Quản lý quan hệ với tác giả, danh mục, nhà xuất bản

### Loan Management (UC-LOAN-001 ~ 010)
- Mượn sách
- Trả sách
- Gia hạn mượn sách
- Lịch sử mượn trả
- Sách quá hạn
- Thống kê mượn trả

### User Management (UC-USER-001 ~ 007)
- Đăng ký user
- Quản lý user
- Đổi mật khẩu
- Quản lý role và status

### Review Management (UC-REVIEW-001 ~ 006)
- Tạo, cập nhật, xóa đánh giá
- Xem đánh giá theo sách/user
- Tính điểm trung bình

### Search (UC-SEARCH-001 ~ 002)
- Tìm kiếm nâng cao (multi-table join)
- Tìm kiếm sách theo nhiều tiêu chí

### Reports (UC-REPORT-001 ~ 003)
- Dashboard statistics
- Loan reports
- Review reports

## 🐛 Troubleshooting

### Database connection error
- Kiểm tra PostgreSQL container đang chạy: `docker ps`
- Kiểm tra port 5433 có bị conflict không
- Kiểm tra credentials trong `application.properties`

### Port 8080 already in use
- Thay đổi port trong `application.properties`: `server.port=8081`

### Build errors
- Xóa `.gradle` folder và build lại
- Kiểm tra Java version: `java -version` (phải là 17+)

## 📄 License

This project is for educational purposes.

## 👥 Contributors

- Development Team

---

**Lưu ý**: Đây là project demo, không sử dụng cho production mà không có các cải tiến về security và performance.
