import { request } from './client.js'

/**
 * Register a new user.
 * @param {import('../types/user.types').UserRequestDTO} body
 * @returns {Promise<import('../types/user.types').UserDTO>}
 */
export function registerUser(body) {
  return request('POST', '/users/register', { body })
}

/**
 * Get paginated users with optional filters.
 * @param {Object} [options]
 * @param {number} [options.page=0]
 * @param {number} [options.size=20]
 * @param {import('../types/enums').UserStatus} [options.status]
 * @param {import('../types/enums').UserRole} [options.role]
 * @returns {Promise<import('../types/common.types').Page<import('../types/user.types').UserDTO>>}
 */
export function getAllUsers(options = {}) {
  const { page = 0, size = 20, status, role } = options
  const query = { page, size }
  if (status) query.status = status
  if (role) query.role = role
  return request('GET', '/users', { query })
}

/**
 * Get user by id.
 * @param {number} id
 * @returns {Promise<import('../types/user.types').UserDTO>}
 */
export function getUserById(id) {
  return request('GET', `/users/${id}`)
}

/**
 * Update user.
 * @param {number} id
 * @param {import('../types/user.types').UserUpdateDTO} body
 * @returns {Promise<import('../types/user.types').UserDTO>}
 */
export function updateUser(id, body) {
  return request('PUT', `/users/${id}`, { body })
}

/**
 * Change user password.
 * @param {number} id
 * @param {import('../types/user.types').ChangePasswordDTO} body
 * @returns {Promise<null>}
 */
export function changePassword(id, body) {
  return request('PUT', `/users/${id}/password`, { body })
}

/**
 * Update user status.
 * @param {number} id
 * @param {import('../types/user.types').UpdateUserStatusDTO} body
 * @returns {Promise<import('../types/user.types').UserDTO>}
 */
export function updateUserStatus(id, body) {
  return request('PUT', `/users/${id}/status`, { body })
}

/**
 * Update user role.
 * @param {number} id
 * @param {import('../types/user.types').UpdateUserRoleDTO} body
 * @returns {Promise<import('../types/user.types').UserDTO>}
 */
export function updateUserRole(id, body) {
  return request('PUT', `/users/${id}/role`, { body })
}
