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
      {error && <p style={{ color: 'var(--danger)' }}>{error}</p>}
      {addError && <p style={{ color: 'var(--danger)' }}>{addError}</p>}
      {lastAddedProductId && !addError && (
        <p style={{ color: 'var(--success)' }}>Added to cart.</p>
      )}

      <ProductTable
        products={products}
        onDelete={handleDelete}
        onAddToCart={handleAddToCart}
        pendingProductId={pendingProductId}
      />
    </section>
  )
}

export default ProductListPage
