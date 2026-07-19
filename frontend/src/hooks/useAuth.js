import { useCallback } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  login,
  logout,
  selectAuthError,
  selectAuthStatus,
  selectIsAuthResolving,
  selectIsLoggedIn,
  selectUsername,
} from '../features/auth/authSlice'

/**
 * Session state and the login/logout actions.
 */
export function useAuth() {
  const dispatch = useDispatch()
  const username = useSelector(selectUsername)
  const status = useSelector(selectAuthStatus)
  const error = useSelector(selectAuthError)
  const isLoggedIn = useSelector(selectIsLoggedIn)
  const isResolving = useSelector(selectIsAuthResolving)

  const signIn = useCallback(
    (credentials) => dispatch(login(credentials)),
    [dispatch],
  )

  const signOut = useCallback(() => dispatch(logout()), [dispatch])

  return {
    username,
    status,
    error,
    isLoggedIn,
    isResolving,
    isSigningIn: status === 'loading',
    signIn,
    signOut,
  }
}
