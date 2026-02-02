import { request } from './client.js'

/**
 * Login with email and password. Does not require Authorization header.
 * @param {{ email: string, password: string }} credentials
 * @returns {Promise<{ accessToken: string, user: { id: number, email: string, fullName: string, role: string } }>}
 */
export function login(credentials) {
  return request('POST', '/auth/login', { body: credentials })
}

/**
 * Get current user from token. Requires Authorization header.
 * @returns {Promise<{ id: number, email: string, fullName: string, role: string }>}
 */
export function getMe() {
  return request('GET', '/auth/me')
}
