import { Link } from 'react-router-dom'
import { formatPrice } from '../utils/format'

/**
 * Confirmation shown immediately after a successful mock payment.
 */
function OrderReceipt({ order, onDismiss }) {
  if (!order) return null

  return (
    <div className="receipt" role="status">
      <div className="receipt-header">
        <div>
          <strong>Payment successful</strong>
          <p className="muted receipt-number">
            Order {order.orderNumber} · {order.status}
          </p>
        </div>
        <button type="button" onClick={onDismiss} aria-label="Dismiss receipt">
          ✕
        </button>
      </div>

      <table className="receipt-table">
        <tbody>
          {order.items.map((item) => (
            <tr key={item.productId}>
              <td>
                {item.productName} × {item.quantity}
              </td>
              <td className="receipt-amount">{formatPrice(item.lineTotal)}</td>
            </tr>
          ))}
          <tr className="receipt-total">
            <td>Total paid</td>
            <td className="receipt-amount">{formatPrice(order.totalAmount)}</td>
          </tr>
        </tbody>
      </table>

      <Link to="/orders">View all orders →</Link>
    </div>
  )
}

export default OrderReceipt
