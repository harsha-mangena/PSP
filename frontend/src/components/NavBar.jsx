import { useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { useCart } from '../hooks/useCart'
import { useAuth } from '../hooks/useAuth'

const linkClass = ({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')

function NavBar() {
  const { itemCount } = useCart()
  const { username, signOut } = useAuth()
  const navigate = useNavigate()
  const [searchTerm, setSearchTerm] = useState('')

  const handleSignOut = async () => {
    await signOut()
    navigate('/login', { replace: true })
  }

  const handleSearchSubmit = (event) => {
    event.preventDefault()
    const params = searchTerm.trim() ? `?search=${encodeURIComponent(searchTerm.trim())}` : ''
    navigate(`/products${params}`)
  }

  return (
    <header className="site-header">
      <div className="site-header-inner">
        <NavLink to="/products" className="brand-logo">
          Bazaar<span>io</span>
        </NavLink>

        <form className="header-search" onSubmit={handleSearchSubmit} role="search">
          <input
            type="search"
            placeholder="Search for products, brands and more"
            value={searchTerm}
            onChange={(event) => setSearchTerm(event.target.value)}
            aria-label="Search products"
          />
          <button type="submit" className="header-search-icon" aria-label="Search" tabIndex={-1}>
            ⌕
          </button>
        </form>

        <nav className="header-actions">
          <NavLink to="/products" className={linkClass} end>
            Products
          </NavLink>
          <NavLink to="/add-product" className={linkClass}>
            Sell
          </NavLink>
          <NavLink to="/cart" className={linkClass}>
            Cart{itemCount > 0 ? <span className="cart-badge">{itemCount}</span> : null}
          </NavLink>
          <NavLink to="/orders" className={linkClass}>
            Orders
          </NavLink>

          <div className="user-chip">
            <span>{username}</span>
            <button type="button" onClick={handleSignOut}>
              Sign out
            </button>
          </div>
        </nav>
      </div>
    </header>
  )
}

export default NavBar
