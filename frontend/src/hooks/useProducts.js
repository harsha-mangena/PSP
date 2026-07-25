import { useCallback, useEffect, useMemo, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useSearchParams } from 'react-router-dom'
import {
  deleteProduct,
  fetchProducts,
  selectPagination,
  selectProducts,
  selectProductsError,
  selectProductsStatus,
} from '../features/products/productsSlice'
import { EMPTY_FILTERS, filterProducts } from '../utils/filterProducts'

/**
 * Owns everything the product list screen needs: initial load, pagination,
 * category, sorting, filtering and row actions. The page component just
 * renders what this returns.
 */
export function useProducts() {
  const dispatch = useDispatch()
  const products = useSelector(selectProducts)
  const pagination = useSelector(selectPagination)
  const status = useSelector(selectProductsStatus)
  const error = useSelector(selectProductsError)
  const [searchParams] = useSearchParams()

  // Picks up ?search= from the header's search bar on first load only - after
  // that the filter panel owns this value.
  const [filters, setFilters] = useState(() => ({
    ...EMPTY_FILTERS,
    search: searchParams.get('search') ?? '',
  }))

  useEffect(() => {
    if (status === 'idle') {
      dispatch(fetchProducts())
    }
  }, [status, dispatch])

  /**
   * Recomputes only when the loaded page or the filters actually change, so
   * unrelated re-renders don't refilter.
   */
  const visibleProducts = useMemo(
    () => filterProducts(products, filters),
    [products, filters],
  )

  const setFilter = useCallback((name, value) => {
    setFilters((previous) => ({ ...previous, [name]: value }))
  }, [])

  const resetFilters = useCallback(() => setFilters(EMPTY_FILTERS), [])

  const goToPage = useCallback((page) => dispatch(fetchProducts({ page })), [dispatch])

  // A larger page size can put the current offset past the end, so go back to page 0.
  const changePageSize = useCallback(
    (size) => dispatch(fetchProducts({ size, page: 0 })),
    [dispatch],
  )

  const changeSort = useCallback(
    (sortBy, direction) => dispatch(fetchProducts({ sortBy, direction, page: 0 })),
    [dispatch],
  )

  // Switching category resets to page 0, same reasoning as page size.
  const changeCategory = useCallback(
    (category) => dispatch(fetchProducts({ category: category || undefined, page: 0 })),
    [dispatch],
  )

  const reload = useCallback(() => dispatch(fetchProducts()), [dispatch])

  const removeProduct = useCallback((id) => dispatch(deleteProduct(id)), [dispatch])

  return {
    products,
    visibleProducts,
    pagination,
    status,
    error,
    isLoading: status === 'loading',
    filters,
    setFilter,
    resetFilters,
    goToPage,
    changePageSize,
    changeSort,
    changeCategory,
    reload,
    removeProduct,
  }
}
