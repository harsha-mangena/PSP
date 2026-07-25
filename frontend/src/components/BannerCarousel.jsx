import { useCallback, useEffect, useRef, useState } from 'react'

const SLIDES = [
  {
    eyebrow: 'Big Storewide Sale',
    headline: <>Up to <em>40% off</em> electronics</>,
    subtext: 'Laptops, earbuds, smart watches and more - today only.',
    cta: 'Shop Electronics',
    category: 'Electronics',
    background: 'linear-gradient(135deg, #0f6b5c 0%, #0a4b40 100%)',
  },
  {
    eyebrow: 'New Season',
    headline: <>Fresh looks for <em>every wardrobe</em></>,
    subtext: 'Everyday fashion essentials, priced to move.',
    cta: 'Shop Fashion',
    category: 'Fashion',
    background: 'linear-gradient(135deg, #b3242e 0%, #7e1620 100%)',
  },
  {
    eyebrow: 'Home Refresh',
    headline: <>Make your kitchen <em>work harder</em></>,
    subtext: 'Air fryers, cookware and everything in between.',
    cta: 'Shop Home & Kitchen',
    category: 'Home & Kitchen',
    background: 'linear-gradient(135deg, #b9791a 0%, #8a5a10 100%)',
  },
  {
    eyebrow: 'Get Moving',
    headline: <>Gear up for your <em>next workout</em></>,
    subtext: 'Yoga mats, dumbbells and gear for every sport.',
    cta: 'Shop Sports & Fitness',
    category: 'Sports & Fitness',
    background: 'linear-gradient(135deg, #22345c 0%, #141f3d 100%)',
  },
]

const AUTO_ADVANCE_MS = 5000

/**
 * Auto-rotating hero banner. Pauses on hover so a reader doesn't lose their
 * place mid-read, and each slide's CTA jumps straight to that category.
 */
function BannerCarousel({ onShopCategory }) {
  const [index, setIndex] = useState(0)
  const [paused, setPaused] = useState(false)
  const timerRef = useRef(null)

  const goTo = useCallback((i) => {
    setIndex((i + SLIDES.length) % SLIDES.length)
  }, [])

  useEffect(() => {
    if (paused) return undefined
    timerRef.current = setInterval(() => {
      setIndex((i) => (i + 1) % SLIDES.length)
    }, AUTO_ADVANCE_MS)
    return () => clearInterval(timerRef.current)
  }, [paused])

  return (
    <div
      className="banner-carousel"
      onMouseEnter={() => setPaused(true)}
      onMouseLeave={() => setPaused(false)}
    >
      {SLIDES.map((slide, i) => (
        <div
          key={slide.category}
          className={`banner-slide${i === index ? ' active' : ''}`}
          style={{ background: slide.background }}
          aria-hidden={i !== index}
        >
          <div>
            <span className="banner-eyebrow">{slide.eyebrow}</span>
            <h2 className="banner-headline">{slide.headline}</h2>
            <p className="banner-subtext">{slide.subtext}</p>
            <button
              type="button"
              className="banner-cta"
              onClick={() => onShopCategory?.(slide.category)}
            >
              {slide.cta} →
            </button>
          </div>
        </div>
      ))}

      <button
        type="button"
        className="banner-arrow prev"
        onClick={() => goTo(index - 1)}
        aria-label="Previous banner"
      >
        ‹
      </button>
      <button
        type="button"
        className="banner-arrow next"
        onClick={() => goTo(index + 1)}
        aria-label="Next banner"
      >
        ›
      </button>

      <div className="banner-dots">
        {SLIDES.map((slide, i) => (
          <button
            key={slide.category}
            type="button"
            className={`banner-dot${i === index ? ' active' : ''}`}
            onClick={() => goTo(i)}
            aria-label={`Go to slide ${i + 1}`}
          />
        ))}
      </div>
    </div>
  )
}

export default BannerCarousel
