/*
 * Script: Lấy 1 tài khoản Admin và 1 tài khoản Member để test
 * Password mặc định (seed): password123
 * Chạy: psql hoặc qua docker exec (từ backend: .\scripts\run_get_test_accounts.ps1)
 */

-- Kết quả gộp: 1 ADMIN + 1 MEMBER (status ACTIVE)
SELECT
  id,
  email,
  full_name,
  role,
  status,
  'password123' AS password_note
FROM (
  (SELECT id, email, full_name, role, status
   FROM users
   WHERE role = 'ADMIN'
     AND status = 'ACTIVE'
   ORDER BY id
   LIMIT 1)
  UNION ALL
  (SELECT id, email, full_name, role, status
   FROM users
   WHERE role = 'MEMBER'
     AND status = 'ACTIVE'
   ORDER BY id
   LIMIT 1)
) AS test_accounts;
