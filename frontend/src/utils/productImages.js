/**
 * Generates a product "photo" as an inline SVG data URI - no external image
 * host, no stock-photo licensing question, and it renders instantly with zero
 * network requests. Each product gets a distinct icon on a category-tinted
 * gradient card, which reads far better in a grid than 25 identical grey
 * boxes while staying entirely original artwork.
 */

const CATEGORY_THEME = {
  Electronics: { from: '#0F6B5C', to: '#0B4F44' },
  Fashion: { from: '#B3242E', to: '#7E1620' },
  'Home & Kitchen': { from: '#C98A1E', to: '#96650F' },
  Books: { from: '#22345C', to: '#141F3D' },
  'Sports & Fitness': { from: '#3C6B35', to: '#264621' },
}

const DEFAULT_THEME = { from: '#4A4A52', to: '#2C2C33' }

// Each entry is the inner SVG markup for a 120x120 glyph, centered later
// inside a 320x320 card. Kept as simple filled/stroked primitives so the set
// stays visually consistent across very different product types.
const ICONS = {
  laptop: `
    <rect x="14" y="18" width="92" height="60" rx="6" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M4 90 L22 78 H98 L116 90 Z" fill="#fff" fill-opacity="0.92"/>
    <rect x="50" y="90" width="20" height="4" rx="2" fill="#fff" fill-opacity="0.6"/>
  `,
  smartphone: `
    <rect x="36" y="6" width="48" height="108" rx="10" fill="none" stroke="#fff" stroke-width="5"/>
    <rect x="52" y="16" width="16" height="3" rx="1.5" fill="#fff" fill-opacity="0.7"/>
    <circle cx="60" cy="103" r="5" fill="none" stroke="#fff" stroke-width="3"/>
  `,
  earbuds: `
    <circle cx="38" cy="46" r="20" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M38 66 Q38 92 30 100" fill="none" stroke="#fff" stroke-width="5" stroke-linecap="round"/>
    <circle cx="82" cy="46" r="20" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M82 66 Q82 92 90 100" fill="none" stroke="#fff" stroke-width="5" stroke-linecap="round"/>
  `,
  smartwatch: `
    <rect x="38" y="6" width="44" height="14" rx="4" fill="#fff" fill-opacity="0.55"/>
    <rect x="38" y="100" width="44" height="14" rx="4" fill="#fff" fill-opacity="0.55"/>
    <rect x="30" y="30" width="60" height="60" rx="14" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M60 48 V60 L70 68" fill="none" stroke="#fff" stroke-width="4" stroke-linecap="round"/>
  `,
  speaker: `
    <rect x="30" y="6" width="60" height="108" rx="12" fill="none" stroke="#fff" stroke-width="5"/>
    <circle cx="60" cy="36" r="10" fill="none" stroke="#fff" stroke-width="4"/>
    <circle cx="60" cy="78" r="18" fill="none" stroke="#fff" stroke-width="4"/>
    <circle cx="60" cy="78" r="6" fill="#fff" fill-opacity="0.7"/>
  `,
  tshirt: `
    <path d="M40 8 L60 20 L80 8 L106 26 L92 46 L82 38 V112 H38 V38 L28 46 L14 26 Z"
      fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
  `,
  dress: `
    <path d="M46 8 H74 L80 24 L100 108 H20 L40 24 Z" fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <path d="M46 8 Q60 22 74 8" fill="none" stroke="#fff" stroke-width="4"/>
  `,
  shoes: `
    <path d="M10 92 Q10 74 28 70 L52 58 Q64 50 78 56 L100 66 Q114 72 114 88 V96 H10 Z"
      fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <path d="M52 58 V78 M78 56 V78" fill="none" stroke="#fff" stroke-width="3" stroke-opacity="0.6"/>
  `,
  wallet: `
    <rect x="14" y="30" width="92" height="64" rx="8" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M14 50 H106" stroke="#fff" stroke-width="3" stroke-opacity="0.6"/>
    <rect x="80" y="56" width="20" height="14" rx="3" fill="#fff" fill-opacity="0.6"/>
  `,
  sunglasses: `
    <circle cx="38" cy="60" r="22" fill="none" stroke="#fff" stroke-width="5"/>
    <circle cx="82" cy="60" r="22" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M60 56 Q60 62 60 56" stroke="#fff" stroke-width="5"/>
    <path d="M58 54 H62" stroke="#fff" stroke-width="5"/>
    <path d="M16 54 L2 48 M104 54 L118 48" stroke="#fff" stroke-width="4" stroke-linecap="round"/>
  `,
  airfryer: `
    <path d="M26 40 H94 L86 106 H34 Z" fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <ellipse cx="60" cy="40" rx="34" ry="10" fill="none" stroke="#fff" stroke-width="5"/>
    <rect x="46" y="58" width="28" height="8" rx="3" fill="#fff" fill-opacity="0.6"/>
  `,
  coffeemaker: `
    <path d="M34 44 H86 L80 100 Q80 110 60 110 Q40 110 40 100 Z" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M86 52 Q108 52 108 70 Q108 88 86 84" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M46 30 Q50 22 46 14 M60 30 Q64 22 60 14 M74 30 Q78 22 74 14"
      fill="none" stroke="#fff" stroke-width="3.5" stroke-linecap="round" stroke-opacity="0.75"/>
  `,
  bedsheet: `
    <path d="M12 70 Q60 50 108 70 V96 Q60 80 12 96 Z" fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <path d="M12 70 Q60 90 108 70" fill="none" stroke="#fff" stroke-width="4" stroke-opacity="0.65"/>
    <path d="M24 36 H96 V62 H24 Z" fill="none" stroke="#fff" stroke-width="4" stroke-opacity="0.5"/>
  `,
  lamp: `
    <path d="M40 12 H80 L94 46 H26 Z" fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <line x1="60" y1="46" x2="60" y2="98" stroke="#fff" stroke-width="5"/>
    <ellipse cx="60" cy="104" rx="26" ry="8" fill="none" stroke="#fff" stroke-width="5"/>
  `,
  cookware: `
    <ellipse cx="56" cy="52" rx="38" ry="14" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M18 52 V72 Q18 92 56 92 Q94 92 94 72 V52" fill="none" stroke="#fff" stroke-width="5"/>
    <rect x="94" y="46" width="26" height="9" rx="4" fill="#fff" fill-opacity="0.7"/>
    <circle cx="56" cy="38" r="4" fill="#fff" fill-opacity="0.7"/>
  `,
  book: `
    <path d="M60 22 Q40 10 16 16 V94 Q40 88 60 100 Q80 88 104 94 V16 Q80 10 60 22 Z"
      fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <line x1="60" y1="22" x2="60" y2="100" stroke="#fff" stroke-width="4" stroke-opacity="0.6"/>
  `,
  yogamat: `
    <rect x="16" y="46" width="70" height="28" rx="14" fill="none" stroke="#fff" stroke-width="5"/>
    <ellipse cx="94" cy="60" rx="14" ry="14" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M30 46 V74 M46 46 V74 M62 46 V74" stroke="#fff" stroke-width="3" stroke-opacity="0.5"/>
  `,
  dumbbell: `
    <rect x="40" y="54" width="40" height="12" rx="4" fill="#fff" fill-opacity="0.85"/>
    <rect x="18" y="40" width="18" height="40" rx="6" fill="none" stroke="#fff" stroke-width="5"/>
    <rect x="84" y="40" width="18" height="40" rx="6" fill="none" stroke="#fff" stroke-width="5"/>
  `,
  cricketbat: `
    <path d="M64 8 L74 8 L80 60 Q80 70 70 74 L58 74 Q52 70 52 62 L58 12 Z"
      fill="none" stroke="#fff" stroke-width="5" stroke-linejoin="round"/>
    <rect x="60" y="74" width="10" height="38" rx="4" fill="#fff" fill-opacity="0.75"/>
  `,
  football: `
    <circle cx="60" cy="60" r="42" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M60 34 L74 46 L68 64 L52 64 L46 46 Z" fill="#fff" fill-opacity="0.85"/>
    <path d="M60 18 V34 M60 86 V102 M22 60 H38 M82 60 H98" stroke="#fff" stroke-width="3" stroke-opacity="0.55"/>
  `,
  helmet: `
    <path d="M18 78 Q18 24 60 24 Q102 24 102 78 Z" fill="none" stroke="#fff" stroke-width="5"/>
    <path d="M18 78 H102 V88 H18 Z" fill="#fff" fill-opacity="0.6"/>
    <circle cx="40" cy="56" r="4" fill="#fff" fill-opacity="0.7"/>
    <circle cx="60" cy="50" r="4" fill="#fff" fill-opacity="0.7"/>
    <circle cx="80" cy="56" r="4" fill="#fff" fill-opacity="0.7"/>
  `,
}

const FALLBACK_ICON = `
  <rect x="24" y="24" width="72" height="72" rx="10" fill="none" stroke="#fff" stroke-width="5"/>
  <circle cx="60" cy="60" r="14" fill="none" stroke="#fff" stroke-width="4"/>
`

function svgToDataUri(svg) {
  const encoded = encodeURIComponent(svg)
    .replace(/'/g, '%27')
    .replace(/"/g, '%22')
  return `data:image/svg+xml;charset=UTF-8,${encoded}`
}

/**
 * Builds a data-URI SVG "product photo" - a category-tinted gradient card
 * with a centered icon glyph. Deterministic: the same (imageKey, category)
 * always produces the same image, so it is safe to call on every render.
 */
export function getProductImageSrc(imageKey, category) {
  const theme = CATEGORY_THEME[category] ?? DEFAULT_THEME
  const icon = ICONS[imageKey] ?? FALLBACK_ICON
  const gradientId = `g-${(imageKey ?? 'default').replace(/[^a-zA-Z0-9]/g, '')}`

  const svg = `
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 320">
  <defs>
    <linearGradient id="${gradientId}" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0%" stop-color="${theme.from}"/>
      <stop offset="100%" stop-color="${theme.to}"/>
    </linearGradient>
  </defs>
  <rect width="320" height="320" fill="url(#${gradientId})"/>
  <circle cx="256" cy="64" r="120" fill="#ffffff" fill-opacity="0.05"/>
  <circle cx="48" cy="272" r="90" fill="#000000" fill-opacity="0.08"/>
  <g transform="translate(100,100)">
    ${icon}
  </g>
</svg>`.trim()

  return svgToDataUri(svg)
}

export function getCategoryColor(category) {
  return (CATEGORY_THEME[category] ?? DEFAULT_THEME).from
}

export const CATEGORY_LIST = Object.keys(CATEGORY_THEME)

// Search terms per icon key, tuned so LoremFlickr's Creative-Commons-sourced
// Flickr photos come back showing the right kind of object. Single words only
// - LoremFlickr ANDs comma-separated tags, and a compound like
// "earbuds,wirelessheadphones" usually matches nothing and silently falls
// back to its generic placeholder photo instead of a real one.
const PHOTO_KEYWORDS = {
  laptop: 'laptop',
  smartphone: 'smartphone',
  earbuds: 'earbuds',
  smartwatch: 'smartwatch',
  speaker: 'speaker',
  tshirt: 'tshirt',
  dress: 'dress',
  shoes: 'runningshoes',
  wallet: 'wallet',
  sunglasses: 'sunglasses',
  airfryer: 'airfryer',
  coffeemaker: 'coffeemaker',
  bedsheet: 'bedsheets',
  lamp: 'tablelamp',
  cookware: 'cookware',
  book: 'books',
  yogamat: 'yogamat',
  dumbbell: 'dumbbells',
  cricketbat: 'cricketbat',
  football: 'soccerball',
  helmet: 'cyclinghelmet',
}

/**
 * A real photo (Creative-Commons Flickr images via LoremFlickr) for the
 * product tile. `lock` pins the pick so it's stable across renders - pass the
 * product id so five products that share an imageKey (e.g. all the books)
 * still each get a distinct photo instead of an identical one.
 */
export function getProductPhotoUrl(imageKey, lock) {
  const keywords = PHOTO_KEYWORDS[imageKey] ?? 'product'
  const lockParam = lock != null ? `?lock=${lock}` : ''
  return `https://loremflickr.com/480/480/${keywords}${lockParam}`
}
