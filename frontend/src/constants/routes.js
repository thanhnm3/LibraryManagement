/**
 * Route paths for navigation (Rule 25: router cho vào file constant).
 */
export const ROUTES = {
  HOME: '/',
  BOOKS: '/books',
  BOOK_DETAIL: '/books/:id',
  SEARCH: '/search',
  JOIN: '/join',
  LOGIN: '/login',
  LOANS: '/loans',
  LOAN_DETAIL: '/loans/:id',
  LOAN_NEW: '/loans/new',
  REVIEWS: '/reviews',

  ADMIN: '/admin',
  ADMIN_BOOKS: '/admin/books',
  ADMIN_BOOK_NEW: '/admin/books/new',
  ADMIN_BOOK_EDIT: '/admin/books/:id/edit',
  ADMIN_AUTHORS: '/admin/authors',
  ADMIN_AUTHOR_NEW: '/admin/authors/new',
  ADMIN_AUTHOR_EDIT: '/admin/authors/:id/edit',
  ADMIN_CATEGORIES: '/admin/categories',
  ADMIN_CATEGORY_NEW: '/admin/categories/new',
  ADMIN_CATEGORY_EDIT: '/admin/categories/:id/edit',
  ADMIN_PUBLISHERS: '/admin/publishers',
  ADMIN_PUBLISHER_NEW: '/admin/publishers/new',
  ADMIN_PUBLISHER_EDIT: '/admin/publishers/:id/edit',
  ADMIN_LOANS: '/admin/loans',
  ADMIN_LOAN_DETAIL: '/admin/loans/:id',
  ADMIN_USERS: '/admin/users',
  ADMIN_USER_EDIT: '/admin/users/:id/edit',
  ADMIN_REPORTS: '/admin/reports',
}
