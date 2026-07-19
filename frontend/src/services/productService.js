import { productApi } from './apiClient'

/**
 * Product API surface. Every product HTTP call in the app goes through here.
 */
export const productService = {
  getAll: async () => {
    const { data } = await productApi.get('/api/products')
    return data
  },

  getPaged: async ({ page = 0, size = 10, sortBy = 'id', direction = 'asc' } = {}) => {
    const { data } = await productApi.get('/api/products/paged', {
      params: { page, size, sortBy, direction },
    })
    return data
  },

  getById: async (id) => {
    const { data } = await productApi.get(`/api/products/${id}`)
    return data
  },

  create: async (product) => {
    const { data } = await productApi.post('/api/products', product)
    return data
  },

  update: async (id, product) => {
    const { data } = await productApi.put(`/api/products/${id}`, product)
    return data
  },

  remove: async (id) => {
    await productApi.delete(`/api/products/${id}`)
    return id
  },
}
