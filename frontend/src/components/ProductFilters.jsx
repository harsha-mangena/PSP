/**
 * Controlled filter inputs. Holds no state of its own — the owning page does.
 */
function ProductFilters({ filters, onChange, onReset, resultCount, totalCount }) {
  const handle = (event) => {
    const { name, value } = event.target
    onChange(name, value)
  }

  return (
    <div className="filters">
      <label>
        <span className="muted">Search by name</span>
        <input
          name="search"
          value={filters.search}
          onChange={handle}
          placeholder="e.g. key"
          aria-label="Search by name"
        />
      </label>
      <label>
        <span className="muted">Min price</span>
        <input
          name="minPrice"
          type="number"
          min="0"
          value={filters.minPrice}
          onChange={handle}
          placeholder="0"
          aria-label="Minimum price"
        />
      </label>
      <label>
        <span className="muted">Max price</span>
        <input
          name="maxPrice"
          type="number"
          min="0"
          value={filters.maxPrice}
          onChange={handle}
          placeholder="9999"
          aria-label="Maximum price"
        />
      </label>
      <label className="filters-checkbox">
        <input
          name="inStockOnly"
          type="checkbox"
          checked={filters.inStockOnly}
          onChange={(event) => onChange('inStockOnly', event.target.checked)}
        />
        <span>In stock only</span>
      </label>
      <button type="button" onClick={onReset}>
        Reset
      </button>

      <p className="muted filters-count">
        Showing {resultCount} of {totalCount}
      </p>
    </div>
  )
}

export default ProductFilters
