import { useEffect, useMemo } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  fetchCart,
  selectCartError,
  selectCartId,
  selectCartItems,
  selectCartStatus,
} from '../features/cart/cartSlice'
import { fetchProducts, selectProducts, selectProductsStatus } from '../features/products/productsSlice'
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
  const products = useSelector(selectProducts)
  const productsStatus = useSelector(selectProductsStatus)

  useEffect(() => {
    dispatch(fetchCart(DEMO_USER_ID))
  }, [dispatch])

  useEffect(() => {
    // Needed for the product-name join when landing on this page directly.
    if (productsStatus === 'idle') {
      dispatch(fetchProducts())
    }
  }, [productsStatus, dispatch])

  const rows = useMemo(() => {
    const byId = new Map(products.map((product) => [product.id, product]))

    return items.map((item) => {
      const product = byId.get(item.productId)
      const unitPrice = Number(product?.price ?? 0)
      return {
        ...item,
        name: product?.name ?? `Product #${item.productId}`,
        unitPrice,
        lineTotal: unitPrice * item.quantity,
      }
    })
  }, [items, products])

  const total = useMemo(
    () => rows.reduce((sum, row) => sum + row.lineTotal, 0),
    [rows],
  )

  return (
    <section>
      <h2>Cart {cartId ? <span className="muted">#{cartId}</span> : null}</h2>
      {error && <p style={{ color: 'var(--danger)' }}>{error}</p>}

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
