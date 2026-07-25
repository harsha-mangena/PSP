import BannerCarousel from '../components/BannerCarousel'
import CategoryRail from '../components/CategoryRail'
import ProductCard from '../components/ProductCard'
import ProductFilters from '../components/ProductFilters'
import Pagination from '../components/Pagination'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import { useProducts } from '../hooks/useProducts'
import { useCart } from '../hooks/useCart'
import { useCategorySections } from '../hooks/useCategorySections'
import { getCategoryColor } from '../utils/productImages'

/**
 * UI only. All data access and logic lives in useProducts / useCart /
 * useCategorySections.
 *
 * Two views share this page: with no category selected it's a storefront
 * home (banner + one row per category), and with a category active it's a
 * standard paginated, sortable, filterable grid for that category - the
 * pagination/sort/filter capability from earlier steps is unchanged, just
 * now scoped to whichever category is picked.
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
    changeCategory,
    reload,
    removeProduct,
  } = useProducts()

  const { addToCart, addError, pendingProductId, lastAddedProductId } = useCart()

  const activeCategory = pagination.category
  const isHomeView = !activeCategory

  const { sections, isLoading: sectionsLoading, error: sectionsError } = useCategorySections({
    enabled: isHomeView,
  })

  const handleSortChange = (event) => {
    const [sortBy, direction] = event.target.value.split(':')
    changeSort(sortBy, direction)
  }

  return (
    <section>
      <BannerCarousel onShopCategory={changeCategory} />
      <CategoryRail activeCategory={activeCategory} onSelect={changeCategory} />

      <ErrorMessage message={addError} />
      {lastAddedProductId && !addError && <p className="success-text">Added to cart.</p>}

      {isHomeView ? (
        <>
          <ErrorMessage message={sectionsError} />
          {sectionsLoading && <Spinner label="Loading products…" />}

          {!sectionsLoading &&
            sections.map(({ category, products: categoryProducts }) => (
              <div
                className="category-section"
                key={category}
                style={{ '--section-color': getCategoryColor(category) }}
              >
                <div className="category-section-header">
                  <h2 className="category-section-title">
                    <span className="dot" />
                    {category}
                  </h2>
                  <button
                    type="button"
                    className="category-section-link"
                    onClick={() => changeCategory(category)}
                  >
                    View all →
                  </button>
                </div>
                <div className="product-grid">
                  {categoryProducts.map((product) => (
                    <ProductCard
                      key={product.id}
                      product={product}
                      onAddToCart={addToCart}
                      isAddingToCart={pendingProductId === product.id}
                    />
                  ))}
                </div>
              </div>
            ))}
        </>
      ) : (
        <>
          <div className="section-header">
            <h2>{activeCategory}</h2>
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
              <div className="product-grid">
                {visibleProducts.map((product) => (
                  <ProductCard
                    key={product.id}
                    product={product}
                    onAddToCart={addToCart}
                    onDelete={removeProduct}
                    isAddingToCart={pendingProductId === product.id}
                  />
                ))}
              </div>
              <Pagination
                pagination={pagination}
                onPageChange={goToPage}
                onSizeChange={changePageSize}
              />
            </>
          )}
        </>
      )}
    </section>
  )
}

export default ProductListPage
