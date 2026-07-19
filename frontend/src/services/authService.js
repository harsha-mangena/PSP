import { cartApi } from './apiClient'

const STORAGE_KEY = 'psp.auth'

/**
 * Session is persisted so a refresh does not log you out. localStorage is
 * readable by any script on the origin — acceptable for a demo token, not for
 * a real credential.
 */
export const authStorage = {
  read: () => {
    try {
      const raw = localStorage.getItem(STORAGE_KEY)
      return raw ? JSON.parse(raw) : null
    } catch {
      // Corrupt or unavailable storage should never break boot.
      return null
    }
  },
  write: (session) => {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(session))
    } catch {
      /* non-fatal */
    }
  },
  clear: () => {
    try {
      localStorage.removeItem(STORAGE_KEY)
    } catch {
      /* non-fatal */
    }
  },
}

export const authService = {
  login: async ({ username, password }) => {
    const { data } = await cartApi.post('/api/auth/login', { username, password })
    return data
  },

  me: async (token) => {
    const { data } = await cartApi.get('/api/auth/me', {
      headers: { Authorization: `Bearer ${token}` },
    })
    return data
  },

  logout: async (token) => {
    await cartApi.post('/api/auth/logout', null, {
      headers: { Authorization: `Bearer ${token}` },
    })
  },
}
