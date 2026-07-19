import { useCallback, useEffect, useState } from 'react'
import ProductTable from '../components/ProductTable'
import { productService } from '../services/productService'
import { extractErrorMessage } from '../services/apiClient'

/**
 * Product list. State moves into Redux at 2C; for now the page holds it
 * locally, but the HTTP calls already live in the service layer.
 */
function ProductListPage() {
  const [products, setProducts] = useState([])
  const [error, setError] = useState(null)

  const loadProducts = useCallback(async () => {
    try {
      setError(null)
      setProducts(await productService.getAll())
    } catch (err) {
      setError(extractErrorMessage(err))
    }
  }, [])

  useEffect(() => {
    loadProducts()
  }, [loadProducts])

  const handleDelete = useCallback(
    async (id) => {
      try {
        await productService.remove(id)
        await loadProducts()
      } catch (err) {
        setError(extractErrorMessage(err))
      }
    },
    [loadProducts],
  )

  return (
    <section>
      <h2>Products</h2>
      {error && <p style={{ color: 'var(--danger)' }}>{error}</p>}
      <ProductTable products={products} onDelete={handleDelete} />
    </section>
  )
}

export default ProductListPage
