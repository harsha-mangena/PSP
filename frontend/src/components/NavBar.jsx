import { NavLink } from 'react-router-dom'
import { useCart } from '../hooks/useCart'

const linkClass = ({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')

function NavBar() {
  const { itemCount } = useCart()

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
      <NavLink to="/orders" className={linkClass}>
        Orders
      </NavLink>
    </nav>
  )
}

export default NavBar
