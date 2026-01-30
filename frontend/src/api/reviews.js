import { request } from './client.js'

/**
 * Create a new review.
 * @param {import('../types/review.types').ReviewRequestDTO} body
 * @returns {Promise<import('../types/review.types').ReviewDTO>}
 */
export function createReview(body) {
  return request('POST', '/reviews', { body })
}

/**
 * Update review.
 * @param {number} reviewId
 * @param {import('../types/review.types').ReviewUpdateDTO} body
 * @param {number} userId
 * @returns {Promise<import('../types/review.types').ReviewDTO>}
 */
export function updateReview(reviewId, body, userId) {
  return request('PUT', `/reviews/${reviewId}`, {
    body,
    query: { userId },
  })
}

/**
 * Delete review.
 * @param {number} reviewId
 * @param {number} userId
 * @param {boolean} [isAdmin=false]
 * @returns {Promise<null>}
 */
export function deleteReview(reviewId, userId, isAdmin = false) {
  return request('DELETE', `/reviews/${reviewId}`, {
    query: { userId, isAdmin },
  })
}

/**
 * Get paginated reviews for a book.
 * @param {number} bookId
 * @param {number} [page=0]
 * @param {number} [size=20]
 * @returns {Promise<import('../types/common.types').Page<import('../types/review.types').ReviewDTO>>}
 */
export function getReviewsByBookId(bookId, page = 0, size = 20) {
  return request('GET', `/reviews/books/${bookId}`, { query: { page, size } })
}

/**
 * Get reviews by user.
 * @param {number} userId
 * @returns {Promise<import('../types/review.types').ReviewDTO[]>}
 */
export function getReviewsByUserId(userId) {
  return request('GET', `/reviews/users/${userId}`)
}

/**
 * Get average rating for a book.
 * @param {number} bookId
 * @returns {Promise<import('../types/review.types').AverageRatingDTO>}
 */
export function getAverageRatingByBookId(bookId) {
  return request('GET', `/reviews/books/${bookId}/average-rating`)
}
