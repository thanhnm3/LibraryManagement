import { request } from './client.js'

/**
 * Get dashboard statistics.
 * @returns {Promise<import('../types/report.types').DashboardStatisticsDTO>}
 */
export function getDashboardStatistics() {
  return request('GET', '/reports/dashboard')
}

/**
 * Get loan report by date range.
 * @param {string} [startDate] - ISO date (YYYY-MM-DD)
 * @param {string} [endDate] - ISO date (YYYY-MM-DD)
 * @returns {Promise<import('../types/loan.types').LoanReportDTO>}
 */
export function getLoanReport(startDate, endDate) {
  const query = {}
  if (startDate) query.startDate = startDate
  if (endDate) query.endDate = endDate
  return request('GET', '/reports/loans', { query })
}

/**
 * Get review report, optionally by book.
 * @param {number} [bookId]
 * @returns {Promise<import('../types/review.types').ReviewReportDTO>}
 */
export function getReviewReport(bookId) {
  const query = bookId != null ? { bookId } : {}
  return request('GET', '/reports/reviews', { query })
}
