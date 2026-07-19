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
import { DEMO_USER_ID } from '../utils/constants'

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

  useEffect(() => {
    if (loadOnMount) {
      dispatch(fetchOrders(DEMO_USER_ID))
    }
  }, [loadOnMount, dispatch])

  const placeOrder = useCallback(async () => {
    const action = await dispatch(checkout(DEMO_USER_ID))

    if (checkout.fulfilled.match(action)) {
      // Checkout consumes the cart and decrements stock, so both are now stale.
      dispatch(fetchCart(DEMO_USER_ID))
      dispatch(fetchProducts())
    }
    return action
  }, [dispatch])

  const dismissReceipt = useCallback(() => dispatch(dismissLastOrder()), [dispatch])

  const reload = useCallback(() => dispatch(fetchOrders(DEMO_USER_ID)), [dispatch])

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
