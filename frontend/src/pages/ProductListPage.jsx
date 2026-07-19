import { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import ProductTable from '../components/ProductTable'
import {
  deleteProduct,
  fetchProducts,
  selectProducts,
  selectProductsError,
  selectProductsStatus,
} from '../features/products/productsSlice'

/**
 * Reads products from the Redux store. The component itself performs no HTTP
 * calls; it only dispatches actions and renders state.
 */
function ProductListPage() {
  const dispatch = useDispatch()
  const products = useSelector(selectProducts)
  const status = useSelector(selectProductsStatus)
  const error = useSelector(selectProductsError)

  useEffect(() => {
    if (status === 'idle') {
      dispatch(fetchProducts())
    }
  }, [status, dispatch])

  const handleDelete = useCallback((id) => dispatch(deleteProduct(id)), [dispatch])

  return (
    <section>
      <h2>Products</h2>
      {error && <p style={{ color: 'var(--danger)' }}>{error}</p>}
      <ProductTable products={products} onDelete={handleDelete} />
    </section>
  )
}

export default ProductListPage
