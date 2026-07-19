import axios from 'axios'

/**
 * Axios instances for the two backend microservices. Components never import
 * axios directly; they go through the service modules in this folder.
 */
const PRODUCT_SERVICE_URL =
  import.meta.env.VITE_PRODUCT_SERVICE_URL ?? 'http://localhost:8081'
const CART_SERVICE_URL =
  import.meta.env.VITE_CART_SERVICE_URL ?? 'http://localhost:8082'

const createClient = (baseURL) =>
  axios.create({
    baseURL,
    headers: { 'Content-Type': 'application/json' },
    timeout: 10000,
  })

export const productApi = createClient(PRODUCT_SERVICE_URL)
export const cartApi = createClient(CART_SERVICE_URL)

/**
 * Turns an axios failure into a plain message string. The backend's
 * GlobalExceptionHandler returns { message, fieldErrors }, so surface those
 * rather than a generic "Request failed with status code 400".
 */
export const extractErrorMessage = (error) => {
  const data = error?.response?.data

  if (data?.fieldErrors) {
    const details = Object.entries(data.fieldErrors)
      .map(([field, message]) => `${field}: ${message}`)
      .join(', ')
    if (details) return details
  }

  if (data?.message) return data.message
  if (error?.code === 'ECONNABORTED') return 'The request timed out.'
  if (!error?.response) return 'Cannot reach the server. Is the backend running?'

  return error.message ?? 'Something went wrong.'
}
