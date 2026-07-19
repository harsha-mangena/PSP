import { useCallback, useEffect, useMemo, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import ProductTable from '../components/ProductTable'
import ProductFilters from '../components/ProductFilters'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import { EMPTY_FILTERS, filterProducts } from '../utils/filterProducts'
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

  const [filters, setFilters] = useState(EMPTY_FILTERS)

  useEffect(() => {
    if (status === 'idle') {
      dispatch(fetchProducts())
    }
  }, [status, dispatch])

  /**
   * Recomputes only when the product list or the filters actually change, so
   * unrelated re-renders (cart updates, spinner toggles) don't refilter.
   */
  const visibleProducts = useMemo(
    () => filterProducts(products, filters),
    [products, filters],
  )

  const handleFilterChange = useCallback((name, value) => {
    setFilters((previous) => ({ ...previous, [name]: value }))
  }, [])

  const handleFilterReset = useCallback(() => setFilters(EMPTY_FILTERS), [])

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
        <>
          <ProductFilters
            filters={filters}
            onChange={handleFilterChange}
            onReset={handleFilterReset}
            resultCount={visibleProducts.length}
            totalCount={products.length}
          />
          <ProductTable
            products={visibleProducts}
            onDelete={handleDelete}
            onAddToCart={handleAddToCart}
            pendingProductId={pendingProductId}
          />
        </>
      )}
    </section>
  )
}

export default ProductListPage
