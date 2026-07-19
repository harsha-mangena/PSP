import { useState } from 'react'
import AddProductPage from './pages/AddProductPage'
import ProductListPage from './pages/ProductListPage'

/**
 * Application shell. Real routing arrives at 2H; this step proves the product
 * list and create flows work against the backend.
 */
function App() {
  // Bumping this key remounts the list so it refetches after a create.
  const [refreshKey, setRefreshKey] = useState(0)

  return (
    <div className="app-shell">
      <h1>Enterprise Store</h1>
      <p className="muted">React 18 + Redux Toolkit frontend for the Product / Cart services.</p>

      <AddProductPage onCreated={() => setRefreshKey((key) => key + 1)} />
      <ProductListPage key={refreshKey} />
    </div>
  )
}

export default App
