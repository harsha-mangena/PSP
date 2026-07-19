import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import ErrorMessage from '../components/ErrorMessage'
import {
  createProduct,
  selectCreateError,
  selectCreateStatus,
  selectLastCreated,
} from '../features/products/productsSlice'

const EMPTY_FORM = { name: '', price: '', stock: '' }

function AddProductPage() {
  const dispatch = useDispatch()
  const createStatus = useSelector(selectCreateStatus)
  const createError = useSelector(selectCreateError)
  const lastCreated = useSelector(selectLastCreated)

  const [form, setForm] = useState(EMPTY_FORM)

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((previous) => ({ ...previous, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()

    const action = await dispatch(
      createProduct({
        name: form.name,
        // The API expects numbers; inputs always yield strings.
        price: Number(form.price),
        stock: Number(form.stock),
      }),
    )

    // Only clear the form when the thunk actually succeeded.
    if (createProduct.fulfilled.match(action)) {
      setForm(EMPTY_FORM)
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
          <button
            type="submit"
            className="primary"
            style={{ alignSelf: 'end' }}
            disabled={createStatus === 'loading'}
          >
            {createStatus === 'loading' ? 'Saving…' : 'Create'}
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
