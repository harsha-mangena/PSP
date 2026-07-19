import { useNavigate } from 'react-router-dom'
import ErrorMessage from '../components/ErrorMessage'
import { useProductForm } from '../hooks/useProductForm'

/**
 * UI only. Form state, coercion and submit handling live in useProductForm.
 */
function AddProductPage() {
  const navigate = useNavigate()
  const { form, handleChange, handleSubmit, isSubmitting, createStatus, createError, lastCreated } =
    useProductForm({ onSuccess: () => navigate('/products') })

  return (
    <section className="card">
      <h2>Add product</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ display: 'grid', gap: 12, gridTemplateColumns: '2fr 1fr 1fr auto' }}>
          <label>
            <span className="muted">Name</span>
            <input name="name" value={form.name} onChange={handleChange} placeholder="Keyboard" />
          </label>
          <label>
            <span className="muted">Price</span>
            <input
              name="price"
              type="number"
              step="0.01"
              min="0"
              value={form.price}
              onChange={handleChange}
              placeholder="89.99"
            />
          </label>
          <label>
            <span className="muted">Stock</span>
            <input
              name="stock"
              type="number"
              min="0"
              value={form.stock}
              onChange={handleChange}
              placeholder="40"
            />
          </label>
          <button type="submit" className="primary" style={{ alignSelf: 'end' }} disabled={isSubmitting}>
            {isSubmitting ? 'Saving…' : 'Create'}
          </button>
        </div>
      </form>

      <ErrorMessage message={createError} />
      {createStatus === 'succeeded' && lastCreated && (
        <p className="success-text">
          Created &quot;{lastCreated.name}&quot; (id {lastCreated.id})
        </p>
      )}
    </section>
  )
}

export default AddProductPage
