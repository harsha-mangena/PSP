import { PAGE_SIZE_OPTIONS } from '../utils/constants'

/**
 * Page navigation driven entirely by the backend's PagedResponse metadata.
 */
function Pagination({ pagination, onPageChange, onSizeChange }) {
  const { page, size, totalPages, totalElements, first, last } = pagination

  if (totalElements === 0) return null

  const pageNumbers = Array.from({ length: totalPages }, (_, index) => index)

  return (
    <nav className="pagination" aria-label="Product pages">
      <button type="button" onClick={() => onPageChange(page - 1)} disabled={first}>
        Prev
      </button>

      <div className="pagination-numbers">
        {pageNumbers.map((number) => (
          <button
            key={number}
            type="button"
            className={number === page ? 'primary' : ''}
            aria-current={number === page ? 'page' : undefined}
            onClick={() => onPageChange(number)}
          >
            {number + 1}
          </button>
        ))}
      </div>

      <button type="button" onClick={() => onPageChange(page + 1)} disabled={last}>
        Next
      </button>

      <label className="pagination-size">
        <span className="muted">Per page</span>
        <select value={size} onChange={(event) => onSizeChange(Number(event.target.value))}>
          {PAGE_SIZE_OPTIONS.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
      </label>

      <span className="muted pagination-info">
        Page {page + 1} of {totalPages} · {totalElements} products
      </span>
    </nav>
  )
}

export default Pagination
