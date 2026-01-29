-- Script tạo dữ liệu random cho database
-- Chạy sau khi initial.sql đã tạo các bảng

-- Tắt các constraint tạm thời để tăng tốc insert
SET session_replication_role = 'replica';

-- 1. Insert Publishers (~10,000 records)
INSERT INTO publishers (name, website, address, created_at)
SELECT 
  'Publisher ' || generate_series AS name,
  'https://www.publisher' || generate_series || '.com' AS website,
  'Address ' || generate_series || ', City ' || (random() * 100)::int || ', Country' AS address,
  CURRENT_TIMESTAMP - (random() * 365 * 5 || ' days')::interval AS created_at
FROM generate_series(1, 10000);

-- 2. Insert Authors (~10,000 records)
INSERT INTO authors (full_name, bio, created_at)
SELECT 
  'Author ' || generate_series || ' ' || 
  CASE (random() * 3)::int
    WHEN 0 THEN 'Smith'
    WHEN 1 THEN 'Johnson'
    WHEN 2 THEN 'Williams'
    ELSE 'Brown'
  END AS full_name,
  'Biography of author ' || generate_series || '. This author has written many books.' AS bio,
  CURRENT_TIMESTAMP - (random() * 365 * 5 || ' days')::interval AS created_at
FROM generate_series(1, 10000);

-- 3. Insert Categories (~100 records - thường ít hơn)
INSERT INTO categories (name, description)
SELECT 
  'Category ' || generate_series AS name,
  'Description for category ' || generate_series AS description
FROM generate_series(1, 100);

-- 4. Insert Users (~10,000 records)
-- Password hash mẫu (bcrypt hash của "password123")
INSERT INTO users (email, password_hash, full_name, status, role, created_at)
SELECT 
  'user' || generate_series || '@example.com' AS email,
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password_hash,
  'User ' || generate_series || ' ' || 
  CASE (random() * 5)::int
    WHEN 0 THEN 'Nguyen'
    WHEN 1 THEN 'Tran'
    WHEN 2 THEN 'Le'
    WHEN 3 THEN 'Pham'
    ELSE 'Hoang'
  END AS full_name,
  CASE (random() * 3)::int
    WHEN 0 THEN 'ACTIVE'
    WHEN 1 THEN 'INACTIVE'
    ELSE 'ACTIVE'
  END AS status,
  CASE 
    WHEN random() < 0.05 THEN 'ADMIN'
    ELSE 'MEMBER'
  END AS role,
  CURRENT_TIMESTAMP - (random() * 365 * 3 || ' days')::interval AS created_at
FROM generate_series(1, 10000);

-- 5. Insert Books (~10,000 records)
INSERT INTO books (title, isbn, publication_year, description, cover_image_url, file_path, publisher_id)
SELECT 
  'Book Title ' || generate_series AS title,
  'ISBN-' || LPAD(generate_series::text, 13, '0') AS isbn,
  (1950 + (random() * 75)::int) AS publication_year,
  'Description for book ' || generate_series || '. This is a great book with interesting content.' AS description,
  'https://example.com/covers/book' || generate_series || '.jpg' AS cover_image_url,
  '/files/book' || generate_series || '.pdf' AS file_path,
  (1 + floor(random() * 10000)::int) AS publisher_id
FROM generate_series(1, 10000);

-- 6. Insert Book_Authors (Many-to-Many: mỗi book có 1-3 authors)
INSERT INTO book_authors (book_id, author_id)
SELECT DISTINCT
  book_id,
  (1 + floor(random() * 10000)::int) AS author_id
FROM (
  SELECT 
    generate_series(1, 10000) AS book_id,
    generate_series(1, 2) AS author_seq
) AS book_author_combinations;

-- 7. Insert Book_Categories (Many-to-Many: mỗi book có 1-2 categories)
INSERT INTO book_categories (book_id, category_id)
SELECT DISTINCT
  book_id,
  (1 + floor(random() * 100)::int) AS category_id
FROM (
  SELECT 
    generate_series(1, 10000) AS book_id,
    generate_series(1, 1) AS category_seq
) AS book_category_combinations;

-- 8. Insert Loans (~100,000 records)
INSERT INTO loans (user_id, book_id, borrow_date, due_date, return_date, status)
WITH loan_data AS (
  SELECT 
    (1 + floor(random() * 10000)::int) AS user_id,
    (1 + floor(random() * 10000)::int) AS book_id,
    CURRENT_TIMESTAMP - (random() * 365 * 2 || ' days')::interval AS borrow_date,
    (floor(random() * 14)::int + 14) AS days_until_due,
    random() < 0.7 AS is_returned
  FROM generate_series(1, 100000)
),
loan_calculated AS (
  SELECT 
    user_id,
    book_id,
    borrow_date,
    borrow_date + (days_until_due || ' days')::interval AS due_date,
    CASE 
      WHEN is_returned THEN borrow_date + (floor(random() * days_until_due)::int || ' days')::interval
      ELSE NULL
    END AS return_date,
    is_returned
  FROM loan_data
)
SELECT 
  user_id,
  book_id,
  borrow_date,
  due_date,
  return_date,
  CASE 
    WHEN return_date IS NOT NULL THEN 'RETURNED'
    WHEN due_date < CURRENT_TIMESTAMP THEN 'OVERDUE'
    ELSE 'BORROWED'
  END AS status
FROM loan_calculated;

-- 9. Insert Reviews (~10,000 records)
-- Mỗi user chỉ review 1 số sách nhất định
INSERT INTO reviews (user_id, book_id, rating, comment, created_at)
SELECT DISTINCT
  (1 + floor(random() * 10000)::int) AS user_id,
  (1 + floor(random() * 10000)::int) AS book_id,
  (1 + floor(random() * 5)::int) AS rating,
  CASE 
    WHEN random() < 0.3 THEN NULL
    ELSE 'This is a review comment for book ' || (1 + floor(random() * 10000)::int) || '. Great book!'
  END AS comment,
  CURRENT_TIMESTAMP - (random() * 365 || ' days')::interval AS created_at
FROM generate_series(1, 10000)
ON CONFLICT (user_id, book_id) DO NOTHING;

-- Bật lại các constraint
SET session_replication_role = 'origin';

-- Update statistics để tối ưu query performance
ANALYZE publishers;
ANALYZE authors;
ANALYZE categories;
ANALYZE books;
ANALYZE book_authors;
ANALYZE book_categories;
ANALYZE users;
ANALYZE loans;
ANALYZE reviews;
