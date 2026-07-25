import { useCallback, useEffect, useState } from 'react'
import { CATEGORY_LIST } from '../utils/productImages'
import { productService } from '../services/productService'
import { extractErrorMessage } from '../services/apiClient'

/**
 * Powers the storefront home view: one row of products per category, fetched
 * in parallel. Deliberately separate from the Redux-managed paginated product
 * list (useProducts) - this is a different shape of data (five short,
 * unpaginated lists) used only for the "browse everything" landing view.
 */
export function useCategorySections({ enabled }) {
  const [sections, setSections] = useState([])
  const [status, setStatus] = useState('idle')
  const [error, setError] = useState(null)
  const [retryToken, setRetryToken] = useState(0)

  const reload = useCallback(() => setRetryToken((token) => token + 1), [])

  useEffect(() => {
    if (!enabled) return

    let cancelled = false
    setStatus('loading')
    setError(null)

    Promise.all(
      CATEGORY_LIST.map((category) =>
        productService.getByCategory(category).then((products) => ({ category, products })),
      ),
    )
      .then((results) => {
        if (!cancelled) {
          setSections(results)
          setStatus('succeeded')
        }
      })
      .catch((err) => {
        if (!cancelled) {
          setError(extractErrorMessage(err))
          setStatus('failed')
        }
      })

    return () => {
      cancelled = true
    }
  }, [enabled, retryToken])

  return { sections, isLoading: status === 'loading', error, reload }
}
