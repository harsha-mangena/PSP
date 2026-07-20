const STORAGE_KEY = 'psp.auth'

/**
 * Session persistence, kept in its own module so the axios client and the auth
 * service can both use it without importing each other.
 *
 * localStorage is readable by any script on the origin — acceptable for a demo
 * token, not for a real credential.
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
