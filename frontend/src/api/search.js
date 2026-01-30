import { request } from './client.js'

/**
 * Advanced search (GET) by category, author, title, userId.
 * @param {Object} [params]
 * @param {string} [params.categoryName]
 * @param {string} [params.authorName]
 * @param {number} [params.userId]
 * @param {string} [params.title]
 * @returns {Promise<import('../types/book.types').BookDTO[]>}
 */
export function advancedSearch(params = {}) {
  const { categoryName, authorName, userId, title } = params
  const query = {}
  if (categoryName) query.categoryName = categoryName
  if (authorName) query.authorName = authorName
  if (userId != null) query.userId = userId
  if (title) query.title = title
  return request('GET', '/search/advanced', { query })
}

/**
 * Search books by criteria (POST) with pagination.
 * @param {import('../types/book.types').BookSearchCriteriaDTO} criteria
 * @param {number} [page=0]
 * @param {number} [size=20]
 * @returns {Promise<import('../types/common.types').Page<import('../types/book.types').BookDTO>>}
 */
export function searchBooks(criteria, page = 0, size = 20) {
  return request('POST', '/search/books', {
    body: criteria,
    query: { page, size },
  })
}
