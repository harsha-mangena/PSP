import AddProductPage from './pages/AddProductPage'
import CartPage from './pages/CartPage'
import ProductListPage from './pages/ProductListPage'

/**
 * Application shell. Real routing arrives at 2H; until then all three screens
 * render on one page so the cart flow can be exercised end to end.
 */
function App() {
  return (
    <div className="app-shell">
      <h1>Enterprise Store</h1>
      <p className="muted">React 18 + Redux Toolkit frontend for the Product / Cart services.</p>

      <AddProductPage />
      <ProductListPage />
      <hr style={{ margin: '32px 0', border: 0, borderTop: '1px solid var(--border)' }} />
      <CartPage />
    </div>
  )
}

export default App
