import { useState } from 'react'
import { productService } from '../services/productService'
import { extractErrorMessage } from '../services/apiClient'

const EMPTY_FORM = { name: '', price: '', stock: '' }

function AddProductPage({ onCreated }) {
  const [form, setForm] = useState(EMPTY_FORM)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((previous) => ({ ...previous, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError(null)
    setSuccess(null)

    try {
      const created = await productService.create({
        name: form.name,
        // The API expects numbers; inputs always yield strings.
        price: Number(form.price),
        stock: Number(form.stock),
      })
      setSuccess(`Created "${created.name}" (id ${created.id})`)
      setForm(EMPTY_FORM)
      onCreated?.()
    } catch (err) {
      setError(extractErrorMessage(err))
    }
  }

  return (
    <section className="card" style={{ marginBottom: 24 }}>
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
          <button type="submit" className="primary" style={{ alignSelf: 'end' }}>
            Create
          </button>
        </div>
      </form>

      {error && <p style={{ color: 'var(--danger)' }}>{error}</p>}
      {success && <p style={{ color: 'var(--success)' }}>{success}</p>}
    </section>
  )
}

export default AddProductPage
