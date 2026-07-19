import { Navigate, useLocation } from 'react-router-dom'
import Spinner from '../components/Spinner'
import { useAuth } from '../hooks/useAuth'

/**
 * Gates a route behind a session.
 *
 * While a stored token is still being checked against the server we render a
 * spinner rather than redirecting, otherwise a refresh would bounce a
 * legitimately logged-in user to the login page.
 */
function ProtectedRoute({ children }) {
  const { isLoggedIn, isResolving } = useAuth()
  const location = useLocation()

  if (isResolving) {
    return <Spinner label="Restoring session…" />
  }

  if (!isLoggedIn) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />
  }

  return children
}

export default ProtectedRoute
