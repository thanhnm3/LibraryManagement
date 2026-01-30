import { request } from './client.js'

/**
 * Create a new book.
 * @param {import('../types/book.types').BookRequestDTO} body
 * @returns {Promise<import('../types/book.types').BookDTO>}
 */
export function createBook(body) {
  return request('POST', '/books', { body })
}

/**
 * Get paginated books.
 * @param {number} [page=0]
 * @param {number} [size=20]
 * @returns {Promise<import('../types/common.types').Page<import('../types/book.types').BookDTO>>}
 */
export function getAllBooks(page = 0, size = 20) {
  return request('GET', '/books', { query: { page, size } })
}

/**
 * Get book by id.
 * @param {number} id
 * @returns {Promise<import('../types/book.types').BookDetailDTO>}
 */
export function getBookById(id) {
  return request('GET', `/books/${id}`)
}

/**
 * Update book.
 * @param {number} id
 * @param {import('../types/book.types').BookUpdateDTO} body
 * @returns {Promise<import('../types/book.types').BookDTO>}
 */
export function updateBook(id, body) {
  return request('PUT', `/books/${id}`, { body })
}

/**
 * Delete book.
 * @param {number} id
 * @returns {Promise<null>}
 */
export function deleteBook(id) {
  return request('DELETE', `/books/${id}`)
}
