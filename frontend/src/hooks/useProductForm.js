import { useCallback, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {
  createProduct,
  selectCreateError,
  selectCreateStatus,
  selectLastCreated,
} from '../features/products/productsSlice'

const EMPTY_FORM = { name: '', price: '', stock: '' }

/**
 * Form state and submit handling for creating a product. Keeps the coercion
 * and success/failure branching out of the component.
 */
export function useProductForm({ onSuccess } = {}) {
  const dispatch = useDispatch()
  const createStatus = useSelector(selectCreateStatus)
  const createError = useSelector(selectCreateError)
  const lastCreated = useSelector(selectLastCreated)

  const [form, setForm] = useState(EMPTY_FORM)

  const handleChange = useCallback((event) => {
    const { name, value } = event.target
    setForm((previous) => ({ ...previous, [name]: value }))
  }, [])

  const reset = useCallback(() => setForm(EMPTY_FORM), [])

  const handleSubmit = useCallback(
    async (event) => {
      event.preventDefault()

      const action = await dispatch(
        createProduct({
          name: form.name,
          // The API expects numbers; inputs always yield strings.
          price: Number(form.price),
          stock: Number(form.stock),
        }),
      )

      // Only clear and continue when the thunk actually succeeded.
      if (createProduct.fulfilled.match(action)) {
        setForm(EMPTY_FORM)
        onSuccess?.(action.payload)
      }
    },
    [dispatch, form, onSuccess],
  )

  return {
    form,
    handleChange,
    handleSubmit,
    reset,
    isSubmitting: createStatus === 'loading',
    createStatus,
    createError,
    lastCreated,
  }
}
