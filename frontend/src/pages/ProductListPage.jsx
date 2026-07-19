import ProductTable from '../components/ProductTable'
import ProductFilters from '../components/ProductFilters'
import Pagination from '../components/Pagination'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import { useProducts } from '../hooks/useProducts'
import { useCart } from '../hooks/useCart'

/**
 * UI only. All data access and logic lives in useProducts / useCart.
 */
function ProductListPage() {
  const {
    products,
    visibleProducts,
    pagination,
    isLoading,
    error,
    filters,
    setFilter,
    resetFilters,
    goToPage,
    changePageSize,
    changeSort,
    reload,
    removeProduct,
  } = useProducts()

  const { addToCart, addError, pendingProductId, lastAddedProductId } = useCart()

  const handleSortChange = (event) => {
    const [sortBy, direction] = event.target.value.split(':')
    changeSort(sortBy, direction)
  }

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

      <ErrorMessage message={error} onRetry={reload} />
      <ErrorMessage message={addError} />
      {lastAddedProductId && !addError && <p className="success-text">Added to cart.</p>}

      {isLoading && <Spinner label="Loading products…" />}

      {!isLoading && !error && (
        <>
          <ProductFilters
            filters={filters}
            onChange={setFilter}
            onReset={resetFilters}
            resultCount={visibleProducts.length}
            totalCount={products.length}
          />
          <ProductTable
            products={visibleProducts}
            onDelete={removeProduct}
            onAddToCart={addToCart}
            pendingProductId={pendingProductId}
          />
          <Pagination
            pagination={pagination}
            onPageChange={goToPage}
            onSizeChange={changePageSize}
          />
        </>
      )}
    </section>
  )
}

export default ProductListPage
