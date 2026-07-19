import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import NavBar from './components/NavBar'
import AppRoutes from './routes/AppRoutes'
import { fetchCart } from './features/cart/cartSlice'
import { DEMO_USER_ID } from './utils/constants'

/**
 * Application shell: persistent header and navigation around the routed view.
 */
function App() {
  const dispatch = useDispatch()

  // Load the cart once at startup so the nav badge is correct on any route.
  useEffect(() => {
    dispatch(fetchCart(DEMO_USER_ID))
  }, [dispatch])

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Enterprise Store</h1>
        <p className="muted">
          React 18 + Redux Toolkit frontend for the Product / Cart microservices.
        </p>
      </header>

      <NavBar />

      <main>
        <AppRoutes />
      </main>
    </div>
  )
}

export default App
