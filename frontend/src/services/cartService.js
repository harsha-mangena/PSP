import { cartApi } from './apiClient'

/**
 * Cart API surface. Adding an item triggers the backend chain:
 * cart-service -> product-service (WebClient) -> Kafka -> product-service consumer.
 */
export const cartService = {
  addItem: async ({ userId, productId, quantity }) => {
    const { data } = await cartApi.post('/api/cart/items', { userId, productId, quantity })
    return data
  },

  getCart: async (userId) => {
    const { data } = await cartApi.get(`/api/cart/${userId}`)
    return data
  },
}
