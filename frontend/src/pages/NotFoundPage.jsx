import { Link } from 'react-router-dom'

function NotFoundPage() {
  return (
    <section className="card">
      <h2>Page not found</h2>
      <p className="muted">That route does not exist.</p>
      <Link to="/products">Back to products</Link>
    </section>
  )
}

export default NotFoundPage
