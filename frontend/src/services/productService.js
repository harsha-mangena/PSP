import { api } from './apiClient'

/**
 * Product API surface. Every product HTTP call in the app goes through here.
 */
export const productService = {
  getAll: async () => {
    const { data } = await api.get('/api/products')
    return data
  },

  getPaged: async ({ page = 0, size = 10, sortBy = 'id', direction = 'asc', category } = {}) => {
    // axios omits params whose value is undefined, so a falsy category means
    // "browse everything" rather than sending an empty string to the server.
    const { data } = await api.get('/api/products/paged', {
      params: { page, size, sortBy, direction, category: category || undefined },
    })
    return data
  },

  getCategories: async () => {
    const { data } = await api.get('/api/products/categories')
    return data
  },

  getByCategory: async (category) => {
    const { data } = await api.get(`/api/products/category/${encodeURIComponent(category)}`)
    return data
  },

  getById: async (id) => {
    const { data } = await api.get(`/api/products/${id}`)
    return data
  },

  create: async (product) => {
    const { data } = await api.post('/api/products', product)
    return data
  },

  update: async (id, product) => {
    const { data } = await api.put(`/api/products/${id}`, product)
    return data
  },

  remove: async (id) => {
    await api.delete(`/api/products/${id}`)
    return id
  },
}
