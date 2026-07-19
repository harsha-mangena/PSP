import { formatPrice, stockLabel } from '../utils/format'

/**
 * Presentational only: renders whatever rows it is handed and reports clicks
 * upward. No fetching, no business rules.
 */
function ProductTable({ products, onDelete, onAddToCart, pendingProductId }) {
  if (products.length === 0) {
    return <p className="muted">No products to show.</p>
  }

  return (
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Price</th>
          <th>Stock</th>
          {(onAddToCart || onDelete) && <th>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {products.map((product) => (
          <tr key={product.id}>
            <td>{product.id}</td>
            <td>{product.name}</td>
            <td>{formatPrice(product.price)}</td>
            <td>{stockLabel(product.stock)}</td>
            {(onAddToCart || onDelete) && (
              <td>
                <div style={{ display: 'flex', gap: 8 }}>
                  {onAddToCart && (
                    <button
                      type="button"
                      className="primary"
                      onClick={() => onAddToCart(product)}
                      disabled={product.stock < 1 || pendingProductId === product.id}
                    >
                      {pendingProductId === product.id ? 'Adding…' : 'Add to cart'}
                    </button>
                  )}
                  {onDelete && (
                    <button type="button" onClick={() => onDelete(product.id)}>
                      Delete
                    </button>
                  )}
                </div>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export default ProductTable
