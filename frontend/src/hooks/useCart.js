import { useCallback, useEffect, useMemo } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  addItemToCart,
  fetchCart,
  selectAddError,
  selectCartError,
  selectCartId,
  selectCartItems,
  selectCartStatus,
  selectLastAddedProductId,
  selectMutateError,
  selectPendingItemId,
  selectPendingProductId,
  updateItemQuantity,
  removeCartItem,
} from '../features/cart/cartSlice'
import { fetchProductsByIds, selectProductsById } from '../features/products/productsSlice'
import { DEMO_USER_ID } from '../utils/constants'

/**
 * Cart state plus the product-name join and money maths. Components consume
 * ready-to-render rows and totals.
 */
export function useCart({ loadOnMount = false } = {}) {
  const dispatch = useDispatch()
  const items = useSelector(selectCartItems)
  const cartId = useSelector(selectCartId)
  const status = useSelector(selectCartStatus)
  const error = useSelector(selectCartError)
  const addError = useSelector(selectAddError)
  const pendingProductId = useSelector(selectPendingProductId)
  const lastAddedProductId = useSelector(selectLastAddedProductId)
  const pendingItemId = useSelector(selectPendingItemId)
  const mutateError = useSelector(selectMutateError)
  const productsById = useSelector(selectProductsById)

  useEffect(() => {
    if (loadOnMount) {
      dispatch(fetchCart(DEMO_USER_ID))
    }
  }, [loadOnMount, dispatch])

  useEffect(() => {
    // Cart items may reference products that are not on the loaded page.
    if (items.length > 0) {
      dispatch(fetchProductsByIds(items.map((item) => item.productId)))
    }
  }, [items, dispatch])

  const rows = useMemo(
    () =>
      items.map((item) => {
        const product = productsById[item.productId]
        const unitPrice = Number(product?.price ?? 0)
        return {
          ...item,
          name: product?.name ?? `Product #${item.productId}`,
          unitPrice,
          lineTotal: unitPrice * item.quantity,
        }
      }),
    [items, productsById],
  )

  const total = useMemo(
    () => rows.reduce((sum, row) => sum + row.lineTotal, 0),
    [rows],
  )

  const itemCount = useMemo(
    () => items.reduce((sum, item) => sum + item.quantity, 0),
    [items],
  )

  const addToCart = useCallback(
    (product, quantity = 1) =>
      dispatch(
        addItemToCart({
          userId: DEMO_USER_ID,
          productId: product.id,
          quantity,
        }),
      ),
    [dispatch],
  )

  const reload = useCallback(() => dispatch(fetchCart(DEMO_USER_ID)), [dispatch])

  const setQuantity = useCallback(
    (itemId, quantity) =>
      dispatch(updateItemQuantity({ userId: DEMO_USER_ID, itemId, quantity })),
    [dispatch],
  )

  const removeItem = useCallback(
    (itemId) => dispatch(removeCartItem({ userId: DEMO_USER_ID, itemId })),
    [dispatch],
  )

  return {
    cartId,
    rows,
    total,
    itemCount,
    status,
    isLoading: status === 'loading',
    error,
    addError,
    mutateError,
    pendingProductId,
    pendingItemId,
    lastAddedProductId,
    addToCart,
    setQuantity,
    removeItem,
    reload,
  }
}
