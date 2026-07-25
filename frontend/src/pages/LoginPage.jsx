import { useState } from 'react'
import { useDispatch } from 'react-redux'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import ErrorMessage from '../components/ErrorMessage'
import { clearError, login } from '../features/auth/authSlice'
import { useAuth } from '../hooks/useAuth'

function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const dispatch = useDispatch()
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
    // Clear a previous failure as soon as the user starts correcting it,
    // otherwise the banner looks like it is rejecting what is on screen now.
    if (error) dispatch(clearError())
  }

  const fillDemoCredentials = () => {
    setForm({ username: 'root', password: 'root1234' })
    if (error) dispatch(clearError())
  }

  const handleSubmit = async (event) => {
    event.preventDefault()

    // Read straight from the form rather than trusting React state: browser
    // autofill writes to the DOM without always firing onChange, which would
    // otherwise submit stale or empty values.
    const data = new FormData(event.currentTarget)
    const credentials = {
      // Whitespace here is always an input accident (usually a copy-paste),
      // never intentional, and silently fails an exact-match check.
      username: String(data.get('username') ?? form.username).trim(),
      password: String(data.get('password') ?? form.password).trim(),
    }

    const action = await signIn(credentials)
    if (login.fulfilled.match(action)) {
      navigate(from, { replace: true })
    }
  }

  return (
    <div className="login-wrap">
      <section className="card login-card">
        <h2 className="brand-logo login-brand">
          Bazaar<span>io</span>
        </h2>
        <p className="muted login-sub">Sign in to continue</p>

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
          <button type="button" className="link-button" onClick={fillDemoCredentials}>
            Fill for me
          </button>
        </p>
      </section>
    </div>
  )
}

export default LoginPage
