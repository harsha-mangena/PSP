/**
 * Verifies React Router v6 navigation: each route renders, URLs update, and
 * navigation happens client-side (no full document reload).
 */
import puppeteer from 'puppeteer-core'
import { seedSession, fetchSession } from './lib/session.mjs'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'

const session = await fetchSession()

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})
const page = await browser.newPage()
 await seedSession(page, session)
const errors = []
let documentLoads = 0
page.on('pageerror', (e) => errors.push(e.message))
page.on('console', (m) => m.type() === 'error' && errors.push(m.text()))
page.on('framenavigated', (frame) => {
  if (frame === page.mainFrame()) documentLoads += 1
})

const heading = () => page.$eval('main h2', (el) => el.innerText.trim())

// Root should redirect to /products
await page.goto('http://localhost:3000/', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 700))
const rootUrl = page.url()
const rootHeading = await heading()

const loadsAfterInitial = documentLoads

// A real document reload would wipe this; surviving it proves client-side nav.
await page.evaluate(() => {
  window.__spaSentinel = 'alive'
})

await page.click('a::-p-text(Add Product)')
await new Promise((r) => setTimeout(r, 500))
const addUrl = page.url()
const addHeading = await heading()

await page.click('.nav-link::-p-text(Cart)')
await new Promise((r) => setTimeout(r, 700))
const cartUrl = page.url()
const cartHeading = await heading()

await page.click('a::-p-text(Products)')
await new Promise((r) => setTimeout(r, 700))
const backUrl = page.url()

const spaLoads = documentLoads - loadsAfterInitial
const sentinelSurvived = await page.evaluate(() => window.__spaSentinel === 'alive')

// Deep link straight to /cart must work on a cold load.
await page.goto('http://localhost:3000/cart', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 700))
const deepHeading = await heading()

// Unknown route -> NotFound
await page.goto('http://localhost:3000/nope', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 500))
const notFoundHeading = await heading()

console.log('ROOT_REDIRECT:', rootUrl, '|', rootHeading)
console.log('ADD_PRODUCT:', addUrl, '|', addHeading)
console.log('CART:', cartUrl, '|', cartHeading)
console.log('BACK_TO_PRODUCTS:', backUrl)
console.log('DEEP_LINK_/cart:', deepHeading)
console.log('UNKNOWN_ROUTE:', notFoundHeading)
console.log('SPA_SENTINEL_SURVIVED_NAV:', sentinelSurvived, '(false would mean a full reload)')
console.log('framenavigated_events:', spaLoads, '(includes same-document history changes)')
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok =
  rootUrl.endsWith('/products') &&
  addUrl.endsWith('/add-product') &&
  cartUrl.endsWith('/cart') &&
  backUrl.endsWith('/products') &&
  addHeading === 'Add product' &&
  deepHeading.startsWith('Cart') &&
  notFoundHeading === 'Page not found' &&
  sentinelSurvived &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
