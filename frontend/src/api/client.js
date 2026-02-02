const AUTH_TOKEN_KEY = 'library_auth_token'
const AUTH_USER_KEY = 'library_auth_user'

/**
 * Get stored token (for client and store).
 * @returns {string|null}
 */
export function getStoredToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY)
}

/**
 * Set or clear stored token/user (used by auth store).
 * @param {string|null} token
 * @param {Object|null} user
 */
export function setStoredAuth(token, user) {
  if (token) {
    localStorage.setItem(AUTH_TOKEN_KEY, token)
  } else {
    localStorage.removeItem(AUTH_TOKEN_KEY)
  }
  if (user != null) {
    localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user))
  } else {
    localStorage.removeItem(AUTH_USER_KEY)
  }
}

/**
 * Clear stored auth (on 401 or logout).
 */
function clearStoredAuth() {
  localStorage.removeItem(AUTH_TOKEN_KEY)
  localStorage.removeItem(AUTH_USER_KEY)
  if (typeof window !== 'undefined' && window.dispatchEvent) {
    window.dispatchEvent(new CustomEvent('auth:unauthorized'))
  }
}

/**
 * Shared HTTP client for API calls. Builds URL, sends JSON, parses response.
 * Adds Authorization: Bearer &lt;token&gt; when token is present. On 401, clears auth and dispatches auth:unauthorized.
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
  const token = getStoredToken()
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
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
  if (response.status === 401) {
    clearStoredAuth()
  }
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
