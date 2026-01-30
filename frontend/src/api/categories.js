import { request } from './client.js'

/**
 * Create a new category.
 * @param {import('../types/category.types').CategoryRequestDTO} body
 * @returns {Promise<import('../types/category.types').CategoryDTO>}
 */
export function createCategory(body) {
  return request('POST', '/categories', { body })
}

/**
 * Get paginated categories.
 * @param {number} [page=0]
 * @param {number} [size=20]
 * @returns {Promise<import('../types/common.types').Page<import('../types/category.types').CategoryDTO>>}
 */
export function getAllCategories(page = 0, size = 20) {
  return request('GET', '/categories', { query: { page, size } })
}

/**
 * Get category by id.
 * @param {number} id
 * @returns {Promise<import('../types/category.types').CategoryDetailDTO>}
 */
export function getCategoryById(id) {
  return request('GET', `/categories/${id}`)
}

/**
 * Update category.
 * @param {number} id
 * @param {import('../types/category.types').CategoryUpdateDTO} body
 * @returns {Promise<import('../types/category.types').CategoryDTO>}
 */
export function updateCategory(id, body) {
  return request('PUT', `/categories/${id}`, { body })
}

/**
 * Delete category.
 * @param {number} id
 * @returns {Promise<null>}
 */
export function deleteCategory(id) {
  return request('DELETE', `/categories/${id}`)
}
