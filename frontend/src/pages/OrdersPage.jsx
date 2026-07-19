import { Link } from 'react-router-dom'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import { useOrders } from '../hooks/useOrders'
import { formatPrice, formatDateTime } from '../utils/format'

/**
 * Order history — the record of successfully posted orders.
 */
function OrdersPage() {
  const { orders, status, isLoading, error, reload } = useOrders({ loadOnMount: true })

  return (
    <section>
      <h2>Orders</h2>

      <ErrorMessage message={error} onRetry={reload} />

      {isLoading && <Spinner label="Loading orders…" />}

      {status === 'succeeded' && orders.length === 0 && (
        <p className="muted">
          No orders yet. <Link to="/products">Browse products</Link> to place one.
        </p>
      )}

      {orders.map((order) => (
        <article key={order.id} className="order-card">
          <header className="order-card-header">
            <div>
              <strong>{order.orderNumber}</strong>
              <p className="muted order-meta">{formatDateTime(order.placedAt)}</p>
            </div>
            <div className="order-card-right">
              <span className="status-badge">{order.status}</span>
              <strong>{formatPrice(order.totalAmount)}</strong>
            </div>
          </header>

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
              {order.items.map((item) => (
                <tr key={item.productId}>
                  <td>{item.productName}</td>
                  <td>{formatPrice(item.unitPrice)}</td>
                  <td>{item.quantity}</td>
                  <td>{formatPrice(item.lineTotal)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </article>
      ))}
    </section>
  )
}

export default OrdersPage
