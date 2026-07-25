import { CATEGORY_LIST, getCategoryColor } from '../utils/productImages'

/**
 * Horizontal quick-nav of category pills. "All" plus the five fixed
 * categories; presentational only, the active state and click handling are
 * owned by the caller.
 */
function CategoryRail({ activeCategory, onSelect }) {
  return (
    <nav className="category-rail" aria-label="Product categories">
      <button
        type="button"
        className={`category-pill${!activeCategory ? ' active' : ''}`}
        onClick={() => onSelect(null)}
      >
        <span className="dot" style={{ '--pill-color': 'var(--ink)' }} />
        All
      </button>
      {CATEGORY_LIST.map((category) => (
        <button
          key={category}
          type="button"
          className={`category-pill${activeCategory === category ? ' active' : ''}`}
          style={{ '--pill-color': getCategoryColor(category) }}
          onClick={() => onSelect(category)}
        >
          <span className="dot" />
          {category}
        </button>
      ))}
    </nav>
  )
}

export default CategoryRail
