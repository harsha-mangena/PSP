import axios from 'axios'
import { authStorage } from '../utils/tokenStorage'

/**
 * Everything goes through the API gateway, which routes to product-service and
 * cart-service by service name via Eureka. The frontend therefore knows one
 * origin, not one per microservice.
 */
const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL ?? 'http://localhost:8080'

export const api = axios.create({
  baseURL: GATEWAY_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
})

/**
 * Attach the bearer token to every outbound call. The gateway rejects
 * unauthenticated requests, so this is what keeps the app working once signed
 * in — and it reads from storage rather than the store to avoid a circular
 * dependency between the client and the auth slice.
 */
api.interceptors.request.use((config) => {
  const session = authStorage.read()
  if (session?.token) {
    config.headers.Authorization = `Bearer ${session.token}`
  }
  return config
})

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
  if (!error?.response) return 'Cannot reach the server. Is the gateway running?'

  return error.message ?? 'Something went wrong.'
}
