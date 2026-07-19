import { NavLink, useNavigate } from 'react-router-dom'
import { useCart } from '../hooks/useCart'
import { useAuth } from '../hooks/useAuth'

const linkClass = ({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')

function NavBar() {
  const { itemCount } = useCart()
  const { username, signOut } = useAuth()
  const navigate = useNavigate()

  const handleSignOut = async () => {
    await signOut()
    navigate('/login', { replace: true })
  }

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

      <div className="navbar-user">
        <span className="muted">
          Signed in as <strong>{username}</strong>
        </span>
        <button type="button" onClick={handleSignOut}>
          Sign out
        </button>
      </div>
    </nav>
  )
}

export default NavBar
