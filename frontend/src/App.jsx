import AddProductPage from './pages/AddProductPage'
import ProductListPage from './pages/ProductListPage'

/**
 * Application shell. Real routing arrives at 2H. The pages no longer need a
 * refresh signal between them: both read from the Redux store, so a successful
 * create updates the list automatically.
 */
function App() {
  return (
    <div className="app-shell">
      <h1>Enterprise Store</h1>
      <p className="muted">React 18 + Redux Toolkit frontend for the Product / Cart services.</p>

      <AddProductPage />
      <ProductListPage />
    </div>
  )
}

export default App
