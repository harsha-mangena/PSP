import { api } from './apiClient'

/**
 * Cart API surface. Adding an item triggers the backend chain:
 * cart-service -> product-service (WebClient) -> Kafka -> product-service consumer.
 */
export const cartService = {
  addItem: async ({ userId, productId, quantity }) => {
    const { data } = await api.post('/api/cart/items', { userId, productId, quantity })
    return data
  },

  getCart: async (userId) => {
    const { data } = await api.get(`/api/cart/${userId}`)
    return data
  },

  updateQuantity: async ({ userId, itemId, quantity }) => {
    const { data } = await api.put(`/api/cart/${userId}/items/${itemId}`, { quantity })
    return data
  },

  removeItem: async ({ userId, itemId }) => {
    const { data } = await api.delete(`/api/cart/${userId}/items/${itemId}`)
    return data
  },
}

/**
 * Orders live in cart-service: checkout consumes the cart and produces an order.
 */
export const orderService = {
  checkout: async (userId) => {
    const { data } = await api.post('/api/orders/checkout', { userId })
    return data
  },

  getOrders: async (userId) => {
    const { data } = await api.get(`/api/orders/${userId}`)
    return data
  },
}
