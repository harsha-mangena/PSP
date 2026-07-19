/**
 * Pure filtering logic, kept out of components so it can be reasoned about and
 * reused independently of React.
 */
export const EMPTY_FILTERS = {
  search: '',
  minPrice: '',
  maxPrice: '',
  inStockOnly: false,
}

export const filterProducts = (products, filters) => {
  const term = filters.search.trim().toLowerCase()
  // Blank inputs must not become 0, which would filter everything out.
  const min = filters.minPrice === '' ? null : Number(filters.minPrice)
  const max = filters.maxPrice === '' ? null : Number(filters.maxPrice)

  return products.filter((product) => {
    if (term && !product.name.toLowerCase().includes(term)) return false

    const price = Number(product.price)
    if (min !== null && Number.isFinite(min) && price < min) return false
    if (max !== null && Number.isFinite(max) && price > max) return false

    if (filters.inStockOnly && !(product.stock > 0)) return false

    return true
  })
}
