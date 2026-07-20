import { api } from './apiClient'
import { authStorage } from '../utils/tokenStorage'

export { authStorage }

export const authService = {
  login: async ({ username, password }) => {
    const { data } = await api.post('/api/auth/login', { username, password })
    return data
  },

  me: async (token) => {
    const { data } = await api.get('/api/auth/me', {
      headers: { Authorization: `Bearer ${token}` },
    })
    return data
  },

  logout: async (token) => {
    await api.post('/api/auth/logout', null, {
      headers: { Authorization: `Bearer ${token}` },
    })
  },
}
