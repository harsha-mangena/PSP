import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import ErrorMessage from '../components/ErrorMessage'
import { login } from '../features/auth/authSlice'
import { useAuth } from '../hooks/useAuth'

function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { isLoggedIn, error, isSigningIn, signIn } = useAuth()

  const [form, setForm] = useState({ username: '', password: '' })

  // Where the guard bounced them from, so login returns them there.
  const from = location.state?.from ?? '/products'

  if (isLoggedIn) {
    return <Navigate to={from} replace />
  }

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((previous) => ({ ...previous, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    const action = await signIn(form)
    if (login.fulfilled.match(action)) {
      navigate(from, { replace: true })
    }
  }

  return (
    <div className="login-wrap">
      <section className="card login-card">
        <h2>Sign in</h2>
        <p className="muted login-sub">Enterprise Store</p>

        <form onSubmit={handleSubmit}>
          <label className="login-field">
            <span className="muted">Username</span>
            <input
              name="username"
              value={form.username}
              onChange={handleChange}
              autoComplete="username"
              autoFocus
            />
          </label>

          <label className="login-field">
            <span className="muted">Password</span>
            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              autoComplete="current-password"
            />
          </label>

          <ErrorMessage message={error} />

          <button type="submit" className="primary login-submit" disabled={isSigningIn}>
            {isSigningIn ? 'Signing in…' : 'Sign in'}
          </button>
        </form>

        <p className="muted login-hint">
          Demo credentials: <code>root</code> / <code>root1234</code>
        </p>
      </section>
    </div>
  )
}

export default LoginPage
