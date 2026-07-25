/**
 * Purely cosmetic marketplace flourishes - a star rating and a "was" price
 * with a discount badge. Neither is real data (the backend has no rating or
 * discount concept), so both are derived deterministically from the product
 * id rather than Math.random(), which would make prices flicker on every
 * re-render. Critically, these numbers are display-only: the actual price
 * used everywhere else (cart, checkout, totals) is always product.price,
 * untouched by this file.
 */

// A small xorshift-ish hash so nearby ids don't produce near-identical
// numbers (id and id+1 should look visually distinct in a grid).
function hash(seed) {
  let x = seed * 2654435761
  x = (x ^ (x >>> 13)) >>> 0
  return x / 4294967295
}

export function getMockRating(productId) {
  const id = Number(productId) || 0
  // Skew toward 3.8-4.9: real marketplaces rarely show a product below ~3.5.
  const rating = 3.8 + hash(id) * 1.1
  return Math.round(rating * 10) / 10
}

export function getMockRatingCount(productId) {
  const id = Number(productId) || 0
  return Math.floor(20 + hash(id + 1) * 2400)
}

/**
 * Returns { originalPrice, discountPercent } - originalPrice is always
 * greater than the real price, discountPercent is 0 for about a third of
 * products (not every item is "on sale").
 */
export function getMockDiscount(price, productId) {
  const id = Number(productId) || 0
  const roll = hash(id + 2)

  if (roll < 0.32) {
    return { originalPrice: null, discountPercent: 0 }
  }

  const discountPercent = Math.round(10 + hash(id + 3) * 35)
  const originalPrice = Number(price) / (1 - discountPercent / 100)
  return { originalPrice, discountPercent }
}
