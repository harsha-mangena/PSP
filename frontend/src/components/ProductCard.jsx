import { getProductImageSrc, getProductPhotoUrl } from '../utils/productImages'
import { getMockDiscount, getMockRating, getMockRatingCount } from '../utils/productDisplay'
import { formatPrice } from '../utils/format'

/**
 * A single storefront tile: image, rating, price (with a cosmetic strike
 * price/discount badge - see utils/productDisplay for why those are safe to
 * treat as decoration only), and the add-to-cart / remove actions.
 */
function ProductCard({ product, onAddToCart, onDelete, isAddingToCart }) {
  const inStock = product.stock > 0
  const rating = getMockRating(product.id)
  const ratingCount = getMockRatingCount(product.id)
  const { originalPrice, discountPercent } = getMockDiscount(product.price, product.id)

  return (
    <article className="product-card" data-testid="product-card" data-product-id={product.id}>
      <div className="product-card-image-wrap">
        <img
          src={getProductPhotoUrl(product.imageKey, product.id)}
          alt={product.name}
          loading="lazy"
          onError={(event) => {
            event.currentTarget.onerror = null
            event.currentTarget.src = getProductImageSrc(product.imageKey, product.category)
          }}
        />
        {discountPercent > 0 && (
          <span className="product-card-discount">{discountPercent}% OFF</span>
        )}
        {!inStock && <div className="product-card-out">Out of stock</div>}
      </div>

      <div className="product-card-body">
        <h3 className="product-card-name" data-testid="product-name">
          {product.name}
        </h3>

        <div className="product-card-rating">
          <span className="product-card-stars">{rating.toFixed(1)} ★</span>
          <span className="product-card-rating-count">({ratingCount.toLocaleString()})</span>
        </div>

        <div className="product-card-price-row">
          <span className="product-card-price" data-testid="product-price">
            {formatPrice(product.price)}
          </span>
          {originalPrice && (
            <span className="product-card-price-original">{formatPrice(originalPrice)}</span>
          )}
          {discountPercent > 0 && (
            <span className="product-card-discount-text">{discountPercent}% off</span>
          )}
        </div>

        <span className={`product-card-stock${product.stock > 0 && product.stock <= 10 ? ' low' : ''}`}>
          {inStock
            ? product.stock <= 10
              ? `Only ${product.stock} left`
              : 'In stock'
            : 'Out of stock'}
        </span>

        <div className="product-card-actions">
          <button
            type="button"
            className="primary"
            onClick={() => onAddToCart(product)}
            disabled={!inStock || isAddingToCart}
          >
            {isAddingToCart ? 'Adding…' : 'Add to Cart'}
          </button>
          {onDelete && (
            <button
              type="button"
              className="product-card-remove"
              onClick={() => onDelete(product.id)}
              aria-label={`Remove ${product.name}`}
              title="Remove listing"
            >
              ✕
            </button>
          )}
        </div>
      </div>
    </article>
  )
}

export default ProductCard
