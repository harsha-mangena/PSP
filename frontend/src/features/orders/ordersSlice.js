import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { orderService } from '../../services/cartService'
import { extractErrorMessage } from '../../services/apiClient'

export const fetchOrders = createAsyncThunk(
  'orders/fetchAll',
  async (userId, { rejectWithValue }) => {
    try {
      return await orderService.getOrders(userId)
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

/**
 * Mock payment. The gateway is fake, but the order, the stock decrement and
 * the Kafka event are all real, so the cart must be re-read afterwards.
 */
export const checkout = createAsyncThunk(
  'orders/checkout',
  async (userId, { rejectWithValue }) => {
    try {
      return await orderService.checkout(userId)
    } catch (error) {
      return rejectWithValue(extractErrorMessage(error))
    }
  },
)

const initialState = {
  items: [],
  status: 'idle',
  error: null,
  checkoutStatus: 'idle',
  checkoutError: null,
  lastOrder: null,
}

const ordersSlice = createSlice({
  name: 'orders',
  initialState,
  reducers: {
    dismissLastOrder(state) {
      state.lastOrder = null
      state.checkoutStatus = 'idle'
    },
    clearCheckoutError(state) {
      state.checkoutError = null
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchOrders.pending, (state) => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(fetchOrders.fulfilled, (state, action) => {
        state.status = 'succeeded'
        state.items = action.payload
      })
      .addCase(fetchOrders.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload ?? 'Failed to load orders'
      })

      .addCase(checkout.pending, (state) => {
        state.checkoutStatus = 'loading'
        state.checkoutError = null
        state.lastOrder = null
      })
      .addCase(checkout.fulfilled, (state, action) => {
        state.checkoutStatus = 'succeeded'
        state.lastOrder = action.payload
        // Newest first, matching the API's ordering.
        state.items.unshift(action.payload)
      })
      .addCase(checkout.rejected, (state, action) => {
        state.checkoutStatus = 'failed'
        state.checkoutError = action.payload ?? 'Payment failed'
      })
  },
})

export const { dismissLastOrder, clearCheckoutError } = ordersSlice.actions

export const selectOrders = (state) => state.orders.items
export const selectOrdersStatus = (state) => state.orders.status
export const selectOrdersError = (state) => state.orders.error
export const selectCheckoutStatus = (state) => state.orders.checkoutStatus
export const selectCheckoutError = (state) => state.orders.checkoutError
export const selectLastOrder = (state) => state.orders.lastOrder

export default ordersSlice.reducer
