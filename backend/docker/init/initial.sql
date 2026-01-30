-- 1. Bảng Nhà xuất bản (Publishers)
-- Tách riêng để đảm bảo 3NF, tránh lặp lại thông tin NXB trong bảng sách.
CREATE TABLE publishers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    website VARCHAR(255),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Tác giả (Authors)
CREATE TABLE authors (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    bio TEXT, -- Tiêu sử ngắn
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bảng Danh mục (Categories)
-- Ví dụ: IT, Fiction, Science, Math
CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- 4. Bảng Sách (Books) - Trung tâm của DB
CREATE TABLE books (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL, -- Mã định danh sách quốc tế
    publication_year INT CHECK (publication_year > 1000),
    description TEXT,
    cover_image_url VARCHAR(500),
    file_path VARCHAR(500), -- Đường dẫn file PDF/Epub nếu là sách số
    
    -- Foreign Key: Publisher (Quan hệ N:1)
    publisher_id BIGINT,
    CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_id) REFERENCES publishers(id)
);

-- 5. Bảng trung gian Sách - Tác giả (Book_Authors)
-- Quan hệ Many-to-Many: Một sách nhiều tác giả, một tác giả nhiều sách.
-- Thách thức Hibernate: Mapping @ManyToMany
CREATE TABLE book_authors (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_ba_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    CONSTRAINT fk_ba_author FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);

-- 6. Bảng trung gian Sách - Danh mục (Book_Categories)
-- Quan hệ Many-to-Many
CREATE TABLE book_categories (
    book_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, category_id),
    CONSTRAINT fk_bc_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    CONSTRAINT fk_bc_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- 7. Bảng Người dùng (Users)
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Lưu password đã hash, không lưu plain text!
    full_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'BANNED', 'INACTIVE')),
    role VARCHAR(20) DEFAULT 'MEMBER' CHECK (role IN ('ADMIN', 'MEMBER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. Bảng Mượn trả (Loans) - Quan trọng nhất về Logic
-- Quản lý ai mượn sách nào, lúc nào, hạn trả, trạng thái.
CREATE TABLE loans (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL, -- Ngày hết hạn
    return_date TIMESTAMP, -- Ngày trả thực tế (NULL nếu chưa trả)
    status VARCHAR(20) DEFAULT 'BORROWED' CHECK (status IN ('BORROWED', 'RETURNED', 'OVERDUE')),
    
    CONSTRAINT fk_loan_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 9. Bảng Đánh giá (Reviews)
CREATE TABLE reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint: Mỗi người chỉ được review 1 sách 1 lần
    UNIQUE (user_id, book_id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Tạo Index để tối ưu tìm kiếm (Fresher nên biết cái này)
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_loans_status ON loans(status);
