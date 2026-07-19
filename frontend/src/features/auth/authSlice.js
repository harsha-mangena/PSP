import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { authService, authStorage } from '../../services/authService'
import { extractErrorMessage } from '../../services/apiClient'

const stored = authStorage.read()

export const login = createAsyncThunk(
  'auth/login',
  async ({ username, password }, { rejectWithValue }) => {
    try {
      const session = await authService.login({ username, password })
      authStorage.write(session)
      return session
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

/**
 * Confirms a persisted token is still valid. Tokens live in memory on the
 * server, so a backend restart invalidates them and we must log out cleanly.
 */
export const restoreSession = createAsyncThunk(
  'auth/restore',
  async (_, { getState, rejectWithValue }) => {
    const { token } = getState().auth
    if (!token) return rejectWithValue(null)

    try {
      const { username } = await authService.me(token)
      return { username, token }
    } catch {
      authStorage.clear()
      return rejectWithValue(null)
    }
  },
)

export const logout = createAsyncThunk('auth/logout', async (_, { getState }) => {
  const { token } = getState().auth
  try {
    if (token) await authService.logout(token)
  } catch {
    // Even if the server call fails, the local session must still end.
  }
  authStorage.clear()
})

const initialState = {
  username: stored?.username ?? null,
  token: stored?.token ?? null,
  // 'unknown' until a stored token has been checked against the server.
  status: stored?.token ? 'unknown' : 'loggedOut',
  error: null,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    clearError(state) {
      state.error = null
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(login.fulfilled, (state, action) => {
        state.status = 'loggedIn'
        state.username = action.payload.username
        state.token = action.payload.token
      })
      .addCase(login.rejected, (state, action) => {
        state.status = 'loggedOut'
        state.username = null
        state.token = null
        state.error = action.payload ?? 'Login failed'
      })

      .addCase(restoreSession.fulfilled, (state, action) => {
        state.status = 'loggedIn'
        state.username = action.payload.username
        state.token = action.payload.token
      })
      .addCase(restoreSession.rejected, (state) => {
        state.status = 'loggedOut'
        state.username = null
        state.token = null
      })

      .addCase(logout.fulfilled, (state) => {
        state.status = 'loggedOut'
        state.username = null
        state.token = null
        state.error = null
      })
  },
})

export const { clearError } = authSlice.actions

export const selectUsername = (state) => state.auth.username
export const selectAuthStatus = (state) => state.auth.status
export const selectAuthError = (state) => state.auth.error
export const selectIsLoggedIn = (state) => state.auth.status === 'loggedIn'
export const selectIsAuthResolving = (state) => state.auth.status === 'unknown'

export default authSlice.reducer
