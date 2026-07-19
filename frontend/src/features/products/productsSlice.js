import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { productService } from '../../services/productService'
import { extractErrorMessage } from '../../services/apiClient'

/**
 * Async thunks own the API interaction. Failures are funnelled through
 * rejectWithValue so reducers always receive a display-ready message.
 */
export const fetchProducts = createAsyncThunk(
  'products/fetchAll',
  async (_, { rejectWithValue }) => {
    try {
      return await productService.getAll()
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

export const createProduct = createAsyncThunk(
  'products/create',
  async (product, { rejectWithValue }) => {
    try {
      return await productService.create(product)
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

export const deleteProduct = createAsyncThunk(
  'products/delete',
  async (id, { rejectWithValue }) => {
    try {
      return await productService.remove(id)
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

const initialState = {
  items: [],
  status: 'idle', // idle | loading | succeeded | failed
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
        state.status = 'succeeded'
        state.items = action.payload
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload ?? 'Failed to load products'
      })

      .addCase(createProduct.pending, (state) => {
        state.createStatus = 'loading'
        state.createError = null
        state.lastCreated = null
      })
      .addCase(createProduct.fulfilled, (state, action) => {
        state.createStatus = 'succeeded'
        state.lastCreated = action.payload
        // Keep the list in sync without a second round trip.
        state.items.push(action.payload)
      })
      .addCase(createProduct.rejected, (state, action) => {
        state.createStatus = 'failed'
        state.createError = action.payload ?? 'Failed to create product'
      })

      .addCase(deleteProduct.fulfilled, (state, action) => {
        state.items = state.items.filter((item) => item.id !== action.payload)
      })
      .addCase(deleteProduct.rejected, (state, action) => {
        state.error = action.payload ?? 'Failed to delete product'
      })
  },
})

export const { clearCreateFeedback } = productsSlice.actions

// Selectors keep component code free of state-shape knowledge.
export const selectProducts = (state) => state.products.items
export const selectProductsStatus = (state) => state.products.status
export const selectProductsError = (state) => state.products.error
export const selectCreateStatus = (state) => state.products.createStatus
export const selectCreateError = (state) => state.products.createError
export const selectLastCreated = (state) => state.products.lastCreated

export default productsSlice.reducer
