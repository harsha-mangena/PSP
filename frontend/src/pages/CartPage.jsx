import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import QuantityStepper from '../components/QuantityStepper'
import OrderReceipt from '../components/OrderReceipt'
import { useCart } from '../hooks/useCart'
import { useOrders } from '../hooks/useOrders'
import { formatPrice } from '../utils/format'

/**
 * UI only. Row assembly and totals come from useCart; checkout from useOrders.
 */
function CartPage() {
  const {
    cartId,
    rows,
    total,
    itemCount,
    status,
    isLoading,
    error,
    mutateError,
    pendingItemId,
    setQuantity,
    removeItem,
    reload,
  } = useCart({ loadOnMount: true })

  const { placeOrder, isPaying, checkoutError, lastOrder, dismissReceipt } = useOrders()

  const isEmpty = status === 'succeeded' && rows.length === 0

  return (
    <section>
      <h2>Cart {cartId ? <span className="muted">#{cartId}</span> : null}</h2>

      {lastOrder && <OrderReceipt order={lastOrder} onDismiss={dismissReceipt} />}

      <ErrorMessage message={error} onRetry={reload} />
      <ErrorMessage message={mutateError} />
      <ErrorMessage message={checkoutError} />

      {isLoading && <Spinner label="Loading cart…" />}

      {isEmpty && !lastOrder && <p className="muted">Your cart is empty.</p>}

      {rows.length > 0 && (
        <>
          <table>
            <thead>
              <tr>
                <th>Product</th>
                <th>Unit price</th>
                <th>Quantity</th>
                <th>Line total</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id}>
                  <td>{row.name}</td>
                  <td>{formatPrice(row.unitPrice)}</td>
                  <td>
                    <QuantityStepper
                      value={row.quantity}
                      disabled={pendingItemId === row.id}
                      onChange={(quantity) => setQuantity(row.id, quantity)}
                    />
                  </td>
                  <td>{formatPrice(row.lineTotal)}</td>
                  <td>
                    <button
                      type="button"
                      onClick={() => removeItem(row.id)}
                      disabled={pendingItemId === row.id}
                      aria-label={`Remove ${row.name} from cart`}
                    >
                      {pendingItemId === row.id ? '…' : 'Remove'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="cart-summary">
            <div>
              <span className="muted">
                {itemCount} item{itemCount === 1 ? '' : 's'}
              </span>
              <strong className="cart-total">Total: {formatPrice(total)}</strong>
            </div>
            <button type="button" className="primary pay-button" onClick={placeOrder} disabled={isPaying}>
              {isPaying ? 'Processing payment…' : `Pay ${formatPrice(total)}`}
            </button>
          </div>
          <p className="muted checkout-note">
            Mock payment — no real gateway. Stock is decremented and the order is
            persisted and published to Kafka.
          </p>
        </>
      )}
    </section>
  )
}

export default CartPage
