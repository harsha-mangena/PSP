import { Navigate, Route, Routes } from 'react-router-dom'
import ProductListPage from '../pages/ProductListPage'
import AddProductPage from '../pages/AddProductPage'
import CartPage from '../pages/CartPage'
import OrdersPage from '../pages/OrdersPage'
import NotFoundPage from '../pages/NotFoundPage'

/**
 * Central route table. Adding a screen means adding one entry here.
 */
function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/products" replace />} />
      <Route path="/products" element={<ProductListPage />} />
      <Route path="/add-product" element={<AddProductPage />} />
      <Route path="/cart" element={<CartPage />} />
      <Route path="/orders" element={<OrdersPage />} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default AppRoutes
