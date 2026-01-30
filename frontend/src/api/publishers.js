import { request } from './client.js'

/**
 * Create a new publisher.
 * @param {import('../types/publisher.types').PublisherRequestDTO} body
 * @returns {Promise<import('../types/publisher.types').PublisherDTO>}
 */
export function createPublisher(body) {
  return request('POST', '/publishers', { body })
}

/**
 * Get paginated publishers.
 * @param {number} [page=0]
 * @param {number} [size=20]
 * @returns {Promise<import('../types/common.types').Page<import('../types/publisher.types').PublisherDTO>>}
 */
export function getAllPublishers(page = 0, size = 20) {
  return request('GET', '/publishers', { query: { page, size } })
}

/**
 * Get publisher by id.
 * @param {number} id
 * @returns {Promise<import('../types/publisher.types').PublisherDetailDTO>}
 */
export function getPublisherById(id) {
  return request('GET', `/publishers/${id}`)
}

/**
 * Update publisher.
 * @param {number} id
 * @param {import('../types/publisher.types').PublisherUpdateDTO} body
 * @returns {Promise<import('../types/publisher.types').PublisherDTO>}
 */
export function updatePublisher(id, body) {
  return request('PUT', `/publishers/${id}`, { body })
}

/**
 * Delete publisher.
 * @param {number} id
 * @returns {Promise<null>}
 */
export function deletePublisher(id) {
  return request('DELETE', `/publishers/${id}`)
}
