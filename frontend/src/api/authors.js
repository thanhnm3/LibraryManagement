import { request } from './client.js'

/**
 * Create a new author.
 * @param {import('../types/author.types').AuthorRequestDTO} body
 * @returns {Promise<import('../types/author.types').AuthorDTO>}
 */
export function createAuthor(body) {
  return request('POST', '/authors', { body })
}

/**
 * Get paginated authors with optional search.
 * @param {number} [page=0] - 0-based page
 * @param {number} [size=20]
 * @param {string} [search]
 * @returns {Promise<import('../types/common.types').Page<import('../types/author.types').AuthorDTO>>}
 */
export function getAllAuthors(page = 0, size = 20, search) {
  const query = { page, size }
  if (search) query.search = search
  return request('GET', '/authors', { query })
}

/**
 * Get author by id.
 * @param {number} id
 * @returns {Promise<import('../types/author.types').AuthorDetailDTO>}
 */
export function getAuthorById(id) {
  return request('GET', `/authors/${id}`)
}

/**
 * Update author.
 * @param {number} id
 * @param {import('../types/author.types').AuthorUpdateDTO} body
 * @returns {Promise<import('../types/author.types').AuthorDTO>}
 */
export function updateAuthor(id, body) {
  return request('PUT', `/authors/${id}`, { body })
}

/**
 * Delete author.
 * @param {number} id
 * @returns {Promise<null>}
 */
export function deleteAuthor(id) {
  return request('DELETE', `/authors/${id}`)
}
