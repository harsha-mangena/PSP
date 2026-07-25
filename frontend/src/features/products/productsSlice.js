import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { productService } from '../../services/productService'
import { extractErrorMessage } from '../../services/apiClient'

/**
 * Products are paginated server-side, so `items` holds the current page only
 * and `pagination` mirrors the backend's PagedResponse envelope.
 */
export const fetchProducts = createAsyncThunk(
  'products/fetchPaged',
  async (overrides = {}, { getState, rejectWithValue }) => {
    const { pagination } = getState().products
    const params = { ...pagination, ...overrides }

    try {
      const data = await productService.getPaged(params)
      return { data, params }
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

/**
 * Loads specific products by id. The cart holds productIds that may not be on
 * the currently loaded page, so it needs a lookup independent of pagination.
 */
export const fetchProductsByIds = createAsyncThunk(
  'products/fetchByIds',
  async (ids, { getState, rejectWithValue }) => {
    const { byId } = getState().products
    const missing = [...new Set(ids)].filter((id) => !byId[id])
    if (missing.length === 0) return []

    try {
      return await Promise.all(missing.map((id) => productService.getById(id)))
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

export const createProduct = createAsyncThunk(
  'products/create',
  async (product, { dispatch, rejectWithValue }) => {
    try {
      const created = await productService.create(product)
      // Re-read the current page so ordering and totals stay authoritative.
      dispatch(fetchProducts())
      return created
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

export const deleteProduct = createAsyncThunk(
  'products/delete',
  async (id, { dispatch, rejectWithValue }) => {
    try {
      await productService.remove(id)
      dispatch(fetchProducts())
      return id
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

const initialState = {
  items: [],
  // id -> product, accumulated across pages so lookups survive pagination.
  byId: {},
  pagination: {
    page: 0,
    size: 5,
    sortBy: 'id',
    direction: 'asc',
    category: null,
    totalPages: 0,
    totalElements: 0,
    first: true,
    last: true,
  },
  status: 'idle',
  error: null,
  createStatus: 'idle',
  createError: null,
  lastCreated: null,
}

const productsSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    clearCreateFeedback(state) {
      state.createStatus = 'idle'
      state.createError = null
      state.lastCreated = null
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProducts.pending, (state) => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        const { data, params } = action.payload
        state.status = 'succeeded'
        state.items = data.content
        data.content.forEach((product) => {
          state.byId[product.id] = product
        })
        state.pagination = {
          // Requested params win for the controls the user drives...
          page: data.page,
          size: data.size,
          sortBy: params.sortBy,
          direction: params.direction,
          category: params.category ?? null,
          // ...while the envelope is authoritative for the totals.
          totalPages: data.totalPages,
          totalElements: data.totalElements,
          first: data.first,
          last: data.last,
        }
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload ?? 'Failed to load products'
      })

      .addCase(fetchProductsByIds.fulfilled, (state, action) => {
        action.payload.forEach((product) => {
          state.byId[product.id] = product
        })
      })

      .addCase(createProduct.pending, (state) => {
        state.createStatus = 'loading'
        state.createError = null
        state.lastCreated = null
      })
      .addCase(createProduct.fulfilled, (state, action) => {
        state.createStatus = 'succeeded'
        state.lastCreated = action.payload
      })
      .addCase(createProduct.rejected, (state, action) => {
        state.createStatus = 'failed'
        state.createError = action.payload ?? 'Failed to create product'
      })

      .addCase(deleteProduct.rejected, (state, action) => {
        state.error = action.payload ?? 'Failed to delete product'
      })
  },
})

export const { clearCreateFeedback } = productsSlice.actions

// Selectors keep component code free of state-shape knowledge.
export const selectProducts = (state) => state.products.items
export const selectProductsById = (state) => state.products.byId
export const selectPagination = (state) => state.products.pagination
export const selectProductsStatus = (state) => state.products.status
export const selectProductsError = (state) => state.products.error
export const selectCreateStatus = (state) => state.products.createStatus
export const selectCreateError = (state) => state.products.createError
export const selectLastCreated = (state) => state.products.lastCreated

export default productsSlice.reducer
