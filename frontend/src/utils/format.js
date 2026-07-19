const currencyFormatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
})

export const formatPrice = (value) => {
  const numeric = Number(value)
  return Number.isFinite(numeric) ? currencyFormatter.format(numeric) : '—'
}

export const stockLabel = (stock) => (stock > 0 ? `${stock} in stock` : 'Out of stock')
