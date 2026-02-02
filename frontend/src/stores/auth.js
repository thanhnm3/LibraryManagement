import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, getMe } from '../api/auth'
import { getStoredToken, setStoredAuth } from '../api/client'

/**
 * Auth store: token, user, login, logout, fetchUser. Persists token/user to localStorage.
 */
export const useAuthStore = defineStore('auth', () => {
  const token = ref(getStoredToken())
  const user = ref(null)

  const storedUser = (() => {
    try {
      const raw = localStorage.getItem('library_auth_user')
      return raw ? JSON.parse(raw) : null
    } catch {
      return null
    }
  })()

  if (storedUser && !user.value) {
    user.value = storedUser
  }

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  /**
   * Login with email and password. Stores token and user, returns user.
   * @param {{ email: string, password: string }} credentials
   */
  async function login(credentials) {
    const res = await apiLogin(credentials)
    token.value = res.accessToken
    user.value = res.user
    setStoredAuth(res.accessToken, res.user)
    return res.user
  }

  /**
   * Clear token and user (logout).
   */
  function logout() {
    token.value = null
    user.value = null
    setStoredAuth(null, null)
  }

  /**
   * Restore user from API using stored token (e.g. after reload).
   */
  async function fetchUser() {
    if (!token.value) return null
    try {
      const me = await getMe()
      user.value = me
      setStoredAuth(token.value, me)
      return me
    } catch {
      logout()
      return null
    }
  }

  return {
    token,
    user,
    isAuthenticated,
    isAdmin,
    login,
    logout,
    fetchUser,
  }
})
