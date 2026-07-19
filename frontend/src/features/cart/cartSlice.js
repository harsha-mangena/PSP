import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { cartService } from '../../services/cartService'
import { extractErrorMessage } from '../../services/apiClient'

export const fetchCart = createAsyncThunk(
  'cart/fetch',
  async (userId, { rejectWithValue }) => {
    try {
      // A user with no cart yet gets an empty cart back, not a 404.
      return await cartService.getCart(userId)
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

export const addItemToCart = createAsyncThunk(
  'cart/addItem',
  async ({ userId, productId, quantity }, { rejectWithValue }) => {
    try {
      return await cartService.addItem({ userId, productId, quantity })
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

const initialState = {
  cartId: null,
  items: [],
  status: 'idle',
  error: null,
  addStatus: 'idle',
  addError: null,
  // Product id currently being added, so only that row's button shows a pending state.
  pendingProductId: null,
  lastAddedProductId: null,
}

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    clearAddFeedback(state) {
      state.addStatus = 'idle'
      state.addError = null
      state.lastAddedProductId = null
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchCart.pending, (state) => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(fetchCart.fulfilled, (state, action) => {
        state.status = 'succeeded'
        state.cartId = action.payload.cartId
        state.items = action.payload.items ?? []
      })
      .addCase(fetchCart.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload ?? 'Failed to load cart'
      })

      .addCase(addItemToCart.pending, (state, action) => {
        state.addStatus = 'loading'
        state.addError = null
        state.lastAddedProductId = null
        state.pendingProductId = action.meta.arg.productId
      })
      .addCase(addItemToCart.fulfilled, (state, action) => {
        state.addStatus = 'succeeded'
        state.cartId = action.payload.cartId
        state.items = action.payload.items ?? []
        state.pendingProductId = null
        state.lastAddedProductId = action.meta.arg.productId
      })
      .addCase(addItemToCart.rejected, (state, action) => {
        state.addStatus = 'failed'
        state.addError = action.payload ?? 'Failed to add item to cart'
        state.pendingProductId = null
      })
  },
})

export const { clearAddFeedback } = cartSlice.actions

export const selectCartItems = (state) => state.cart.items
export const selectCartId = (state) => state.cart.cartId
export const selectCartStatus = (state) => state.cart.status
export const selectCartError = (state) => state.cart.error
export const selectAddStatus = (state) => state.cart.addStatus
export const selectAddError = (state) => state.cart.addError
export const selectPendingProductId = (state) => state.cart.pendingProductId
export const selectLastAddedProductId = (state) => state.cart.lastAddedProductId

export default cartSlice.reducer
