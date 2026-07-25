/**
 * Clicks "Add to Cart" on the first product card in the storefront home and
 * asserts the cart page's line-item table updates.
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
page.on('pageerror', (e) => errors.push(e.message))
page.on('console', (m) => m.type() === 'error' && errors.push(m.text()))

await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 1200))

const cartRowsBefore = await page.$$eval('table tbody tr', (rows) => rows.length)

const clickedName = await page.evaluate(() => {
  const card = document.querySelector('.product-card')
  const name = card?.querySelector('[data-testid="product-name"]')?.innerText.trim()
  card?.querySelector('.product-card-actions button.primary')?.click()
  return name
})

await new Promise((r) => setTimeout(r, 2000))
await page.goto('http://localhost:3000/cart', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 800))

const cartRowsAfter = await page.$$eval('table tbody tr', (rows) =>
  rows.map((tr) => [...tr.querySelectorAll('td')].map((td) => td.innerText.trim())),
)

console.log('CLICKED_PRODUCT:', clickedName)
console.log('CART_ROWS_BEFORE:', cartRowsBefore)
console.log('CART_ROWS_AFTER:', JSON.stringify(cartRowsAfter))
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok =
  !!clickedName &&
  cartRowsAfter.length > 0 &&
  cartRowsAfter.some((row) => row[0] === clickedName) &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
