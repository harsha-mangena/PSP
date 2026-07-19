import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import NavBar from './components/NavBar'
import AppRoutes from './routes/AppRoutes'
import { restoreSession } from './features/auth/authSlice'
import { fetchCart } from './features/cart/cartSlice'
import { useAuth } from './hooks/useAuth'

/**
 * Application shell: persistent header and navigation around the routed view.
 */
function App() {
  const dispatch = useDispatch()
  const { isLoggedIn, isResolving, username } = useAuth()

  // Validate any persisted token once at startup.
  useEffect(() => {
    if (isResolving) {
      dispatch(restoreSession())
    }
  }, [isResolving, dispatch])

  // Load the cart once signed in so the nav badge is correct on any route.
  useEffect(() => {
    if (isLoggedIn && username) {
      dispatch(fetchCart(username))
    }
  }, [isLoggedIn, username, dispatch])

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Enterprise Store</h1>
        <p className="muted">
          React 18 + Redux Toolkit frontend for the Product / Cart microservices.
        </p>
      </header>

      {isLoggedIn && <NavBar />}

      <main>
        <AppRoutes />
      </main>
    </div>
  )
}

export default App
