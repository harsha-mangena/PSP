import { useCallback, useEffect, useMemo, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import ProductTable from '../components/ProductTable'
import ProductFilters from '../components/ProductFilters'
import Pagination from '../components/Pagination'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import {
  deleteProduct,
  fetchProducts,
  selectPagination,
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
import { EMPTY_FILTERS, filterProducts } from '../utils/filterProducts'

/**
 * Reads products from the Redux store. The component performs no HTTP calls;
 * it only dispatches actions and renders state.
 *
 * Pagination and sorting are server-side (2G); the search/price filters (2F)
 * refine the page currently loaded.
 */
function ProductListPage() {
  const dispatch = useDispatch()
  const products = useSelector(selectProducts)
  const pagination = useSelector(selectPagination)
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
   * Recomputes only when the loaded page or the filters actually change, so
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

  const handlePageChange = useCallback(
    (page) => dispatch(fetchProducts({ page })),
    [dispatch],
  )

  // A larger page size can put the current offset past the end, so go back to page 0.
  const handleSizeChange = useCallback(
    (size) => dispatch(fetchProducts({ size, page: 0 })),
    [dispatch],
  )

  const handleSortChange = useCallback(
    (event) => {
      const [sortBy, direction] = event.target.value.split(':')
      dispatch(fetchProducts({ sortBy, direction, page: 0 }))
    },
    [dispatch],
  )

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
      <div className="section-header">
        <h2>Products</h2>
        <label className="sort-control">
          <span className="muted">Sort</span>
          <select
            value={`${pagination.sortBy}:${pagination.direction}`}
            onChange={handleSortChange}
          >
            <option value="id:asc">ID ↑</option>
            <option value="name:asc">Name A–Z</option>
            <option value="name:desc">Name Z–A</option>
            <option value="price:asc">Price low → high</option>
            <option value="price:desc">Price high → low</option>
            <option value="stock:asc">Stock low → high</option>
          </select>
        </label>
      </div>

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
          <Pagination
            pagination={pagination}
            onPageChange={handlePageChange}
            onSizeChange={handleSizeChange}
          />
        </>
      )}
    </section>
  )
}

export default ProductListPage
