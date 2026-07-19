import { useCallback, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  checkout,
  dismissLastOrder,
  fetchOrders,
  selectCheckoutError,
  selectCheckoutStatus,
  selectLastOrder,
  selectOrders,
  selectOrdersError,
  selectOrdersStatus,
} from '../features/orders/ordersSlice'
import { fetchCart } from '../features/cart/cartSlice'
import { fetchProducts } from '../features/products/productsSlice'
import { selectUsername } from '../features/auth/authSlice'

/**
 * Order history and the mock checkout.
 */
export function useOrders({ loadOnMount = false } = {}) {
  const dispatch = useDispatch()
  const orders = useSelector(selectOrders)
  const status = useSelector(selectOrdersStatus)
  const error = useSelector(selectOrdersError)
  const checkoutStatus = useSelector(selectCheckoutStatus)
  const checkoutError = useSelector(selectCheckoutError)
  const lastOrder = useSelector(selectLastOrder)
  // Orders are scoped to whoever is signed in.
  const userId = useSelector(selectUsername)

  useEffect(() => {
    if (loadOnMount && userId) {
      dispatch(fetchOrders(userId))
    }
  }, [loadOnMount, userId, dispatch])

  const placeOrder = useCallback(async () => {
    const action = await dispatch(checkout(userId))

    if (checkout.fulfilled.match(action)) {
      // Checkout consumes the cart and decrements stock, so both are now stale.
      dispatch(fetchCart(userId))
      dispatch(fetchProducts())
    }
    return action
  }, [dispatch, userId])

  const dismissReceipt = useCallback(() => dispatch(dismissLastOrder()), [dispatch])

  const reload = useCallback(() => dispatch(fetchOrders(userId)), [dispatch, userId])

  return {
    orders,
    status,
    isLoading: status === 'loading',
    error,
    isPaying: checkoutStatus === 'loading',
    checkoutStatus,
    checkoutError,
    lastOrder,
    placeOrder,
    dismissReceipt,
    reload,
  }
}
