import { useEffect, useMemo } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Spinner from '../components/Spinner'
import ErrorMessage from '../components/ErrorMessage'
import {
  fetchCart,
  selectCartError,
  selectCartId,
  selectCartItems,
  selectCartStatus,
} from '../features/cart/cartSlice'
import { fetchProductsByIds, selectProductsById } from '../features/products/productsSlice'
import { DEMO_USER_ID } from '../utils/constants'
import { formatPrice } from '../utils/format'

/**
 * The cart API returns productId only, so product names and prices are joined
 * in from the products slice.
 */
function CartPage() {
  const dispatch = useDispatch()
  const items = useSelector(selectCartItems)
  const cartId = useSelector(selectCartId)
  const status = useSelector(selectCartStatus)
  const error = useSelector(selectCartError)
  const productsById = useSelector(selectProductsById)

  useEffect(() => {
    dispatch(fetchCart(DEMO_USER_ID))
  }, [dispatch])

  useEffect(() => {
    // Cart items may reference products that are not on the loaded page.
    if (items.length > 0) {
      dispatch(fetchProductsByIds(items.map((item) => item.productId)))
    }
  }, [items, dispatch])

  const rows = useMemo(() => {
    return items.map((item) => {
      const product = productsById[item.productId]
      const unitPrice = Number(product?.price ?? 0)
      return {
        ...item,
        name: product?.name ?? `Product #${item.productId}`,
        unitPrice,
        lineTotal: unitPrice * item.quantity,
      }
    })
  }, [items, productsById])

  const total = useMemo(
    () => rows.reduce((sum, row) => sum + row.lineTotal, 0),
    [rows],
  )

  return (
    <section>
      <h2>Cart {cartId ? <span className="muted">#{cartId}</span> : null}</h2>

      <ErrorMessage
        message={error}
        onRetry={() => dispatch(fetchCart(DEMO_USER_ID))}
      />

      {status === 'loading' && <Spinner label="Loading cart…" />}

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
          <p style={{ textAlign: 'right', fontWeight: 600 }}>
            Total: {formatPrice(total)}
          </p>
        </>
      )}
    </section>
  )
}

export default CartPage
