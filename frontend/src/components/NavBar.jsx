import { NavLink } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { selectCartItems } from '../features/cart/cartSlice'

const linkClass = ({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')

function NavBar() {
  const cartItems = useSelector(selectCartItems)
  const itemCount = cartItems.reduce((sum, item) => sum + item.quantity, 0)

  return (
    <nav className="navbar">
      <NavLink to="/products" className={linkClass}>
        Products
      </NavLink>
      <NavLink to="/add-product" className={linkClass}>
        Add Product
      </NavLink>
      <NavLink to="/cart" className={linkClass}>
        Cart{itemCount > 0 ? ` (${itemCount})` : ''}
      </NavLink>
    </nav>
  )
}

export default NavBar
