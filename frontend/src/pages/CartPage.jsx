import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import { useCart } from '../hooks/useCart'
import { formatPrice } from '../utils/format'

/**
 * UI only. Row assembly, the product-name join and totals come from useCart.
 */
function CartPage() {
  const { cartId, rows, total, status, isLoading, error, reload } = useCart({
    loadOnMount: true,
  })

  return (
    <section>
      <h2>Cart {cartId ? <span className="muted">#{cartId}</span> : null}</h2>

      <ErrorMessage message={error} onRetry={reload} />

      {isLoading && <Spinner label="Loading cart…" />}

      {status === 'succeeded' && rows.length === 0 && (
        <p className="muted">Your cart is empty.</p>
      )}

      {rows.length > 0 && (
        <>
          <table>
            <thead>
              <tr>
                <th>Product</th>
                <th>Unit price</th>
                <th>Qty</th>
                <th>Line total</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id}>
                  <td>{row.name}</td>
                  <td>{formatPrice(row.unitPrice)}</td>
                  <td>{row.quantity}</td>
                  <td>{formatPrice(row.lineTotal)}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <p style={{ textAlign: 'right', fontWeight: 600 }}>Total: {formatPrice(total)}</p>
        </>
      )}
    </section>
  )
}

export default CartPage
