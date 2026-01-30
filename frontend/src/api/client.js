/**
 * Shared HTTP client for API calls. Builds URL, sends JSON, parses response.
 * @param {string} method - HTTP method
 * @param {string} path - Path relative to /api (e.g. '/authors')
 * @param {Object} [options] - Optional body and query
 * @param {Object} [options.body] - Request body (sent as JSON)
 * @param {Record<string, string|number|boolean|undefined>} [options.query] - Query params
 * @returns {Promise<Object|null>} - Parsed JSON or null for 204
 */
export async function request(method, path, options = {}) {
  const { body, query } = options
  const base = '/api'
  let url = `${base}${path}`
  if (query && Object.keys(query).length > 0) {
    const searchParams = new URLSearchParams()
    for (const [key, value] of Object.entries(query)) {
      if (value !== undefined && value !== null && value !== '') {
        searchParams.append(key, String(value))
      }
    }
    const queryString = searchParams.toString()
    if (queryString) {
      url += `?${queryString}`
    }
  }
  const headers = {}
  if (body !== undefined && method !== 'GET') {
    headers['Content-Type'] = 'application/json'
  }
  const init = {
    method,
    headers,
  }
  if (body !== undefined && method !== 'GET') {
    init.body = JSON.stringify(body)
  }
  const response = await fetch(url, init)
  if (response.status === 204) {
    return null
  }
  const data = await response.json().catch(() => ({}))
  if (!response.ok) {
    const error = new Error(data.message || response.statusText || 'Request failed')
    error.status = response.status
    error.data = data
    throw error
  }
  return data
}
