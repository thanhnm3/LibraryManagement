import { request } from './client.js'

/**
 * Create a new loan (borrow book).
 * @param {import('../types/loan.types').LoanRequestDTO} body
 * @returns {Promise<import('../types/loan.types').LoanDTO>}
 */
export function borrowBook(body) {
  return request('POST', '/loans', { body })
}

/**
 * Return book.
 * @param {number} loanId
 * @returns {Promise<import('../types/loan.types').LoanDTO>}
 */
export function returnBook(loanId) {
  return request('PUT', `/loans/${loanId}/return`)
}

/**
 * Renew loan.
 * @param {number} loanId
 * @param {import('../types/loan.types').LoanRenewalRequestDTO} body
 * @returns {Promise<import('../types/loan.types').LoanDTO>}
 */
export function renewLoan(loanId, body) {
  return request('PUT', `/loans/${loanId}/renew`, { body })
}

/**
 * Get paginated loans with optional filters.
 * @param {Object} [options]
 * @param {import('../types/enums').LoanStatus} [options.status]
 * @param {number} [options.userId]
 * @param {number} [options.bookId]
 * @param {number} [options.page=0]
 * @param {number} [options.size=20]
 * @returns {Promise<import('../types/common.types').Page<import('../types/loan.types').LoanDTO>>}
 */
export function getAllLoans(options = {}) {
  const { status, userId, bookId, page = 0, size = 20 } = options
  const query = { page, size }
  if (status) query.status = status
  if (userId != null) query.userId = userId
  if (bookId != null) query.bookId = bookId
  return request('GET', '/loans', { query })
}

/**
 * Get loan by id.
 * @param {number} loanId
 * @returns {Promise<import('../types/loan.types').LoanDetailDTO>}
 */
export function getLoanById(loanId) {
  return request('GET', `/loans/${loanId}`)
}

/**
 * Get loan history for user.
 * @param {number} userId
 * @returns {Promise<import('../types/loan.types').LoanDTO[]>}
 */
export function getLoanHistoryByUserId(userId) {
  return request('GET', `/loans/users/${userId}/history`)
}

/**
 * Get active loans for user.
 * @param {number} userId
 * @returns {Promise<import('../types/loan.types').LoanDTO[]>}
 */
export function getActiveLoansByUserId(userId) {
  return request('GET', `/loans/users/${userId}/active`)
}

/**
 * Get overdue loans, optionally filtered by user.
 * @param {number} [userId]
 * @returns {Promise<import('../types/loan.types').LoanDTO[]>}
 */
export function getOverdueLoans(userId) {
  const query = userId != null ? { userId } : {}
  return request('GET', '/loans/overdue', { query })
}

/**
 * Get loan statistics, optionally by date range.
 * @param {string} [startDate] - ISO date-time
 * @param {string} [endDate] - ISO date-time
 * @returns {Promise<import('../types/loan.types').LoanStatisticsDTO>}
 */
export function getLoanStatistics(startDate, endDate) {
  const query = {}
  if (startDate) query.startDate = startDate
  if (endDate) query.endDate = endDate
  return request('GET', '/loans/statistics', { query })
}
