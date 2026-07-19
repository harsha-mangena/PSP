import { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import ProductTable from '../components/ProductTable'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import {
  deleteProduct,
  fetchProducts,
  selectProducts,
  selectProductsError,
  selectProductsStatus,
} from '../features/products/productsSlice'
import {
  addItemToCart,
  selectAddError,
  selectLastAddedProductId,
  selectPendingProductId,
} from '../features/cart/cartSlice'
import { DEMO_USER_ID } from '../utils/constants'

/**
 * Reads products from the Redux store. The component itself performs no HTTP
 * calls; it only dispatches actions and renders state.
 */
function ProductListPage() {
  const dispatch = useDispatch()
  const products = useSelector(selectProducts)
  const status = useSelector(selectProductsStatus)
  const error = useSelector(selectProductsError)
  const addError = useSelector(selectAddError)
  const pendingProductId = useSelector(selectPendingProductId)
  const lastAddedProductId = useSelector(selectLastAddedProductId)

  useEffect(() => {
    if (status === 'idle') {
      dispatch(fetchProducts())
    }
  }, [status, dispatch])

  const handleRetry = useCallback(() => dispatch(fetchProducts()), [dispatch])

  const handleDelete = useCallback((id) => dispatch(deleteProduct(id)), [dispatch])

  const handleAddToCart = useCallback(
    (product) =>
      dispatch(
        addItemToCart({
          userId: DEMO_USER_ID,
          productId: product.id,
          quantity: 1,
        }),
      ),
    [dispatch],
  )

  return (
    <section>
      <h2>Products</h2>

      <ErrorMessage message={error} onRetry={handleRetry} />
      <ErrorMessage message={addError} />
      {lastAddedProductId && !addError && <p className="success-text">Added to cart.</p>}

      {status === 'loading' && <Spinner label="Loading products…" />}

      {status !== 'loading' && !error && (
        <ProductTable
          products={products}
          onDelete={handleDelete}
          onAddToCart={handleAddToCart}
          pendingProductId={pendingProductId}
        />
      )}
    </section>
  )
}

export default ProductListPage
