import { Navigate, Route, Routes } from 'react-router-dom'
import ProductListPage from '../pages/ProductListPage'
import AddProductPage from '../pages/AddProductPage'
import CartPage from '../pages/CartPage'
import OrdersPage from '../pages/OrdersPage'
import LoginPage from '../pages/LoginPage'
import NotFoundPage from '../pages/NotFoundPage'
import ProtectedRoute from './ProtectedRoute'

/**
 * Central route table. Everything except /login sits behind a session.
 */
function AppRoutes() {
  const protect = (element) => <ProtectedRoute>{element}</ProtectedRoute>

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<Navigate to="/products" replace />} />
      <Route path="/products" element={protect(<ProductListPage />)} />
      <Route path="/add-product" element={protect(<AddProductPage />)} />
      <Route path="/cart" element={protect(<CartPage />)} />
      <Route path="/orders" element={protect(<OrdersPage />)} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default AppRoutes
