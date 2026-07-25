/**
 * Exercises search + price filters within a category and asserts the grid
 * narrows in real time without any additional network requests (filtering is
 * client-side/useMemo). Filters only render once a category is selected, so
 * this first clicks the Electronics pill.
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
await new Promise((r) => setTimeout(r, 1000))

await page.evaluate(() => {
  const pill = [...document.querySelectorAll('.category-pill')].find(
    (el) => el.innerText.trim() === 'Electronics',
  )
  pill?.click()
})
await new Promise((r) => setTimeout(r, 900))

const names = () =>
  page.$$eval('.product-card [data-testid="product-name"]', (els) =>
    els.map((el) => el.innerText.trim()),
  )

const baseline = await names()

// Count network calls from here on, to prove filtering does not refetch.
let apiCalls = 0
page.on('request', (req) => {
  if (req.url().includes('/api/')) apiCalls += 1
})

// --- search
await page.type('input[name="search"]', 'Pro')
await new Promise((r) => setTimeout(r, 500))
const afterPro = await names()

await page.$eval('input[name="search"]', (el) => {
  el.value = ''
  el.dispatchEvent(new Event('input', { bubbles: true }))
})
await page.type('input[name="search"]', 'Laptop')
await new Promise((r) => setTimeout(r, 500))
const afterLaptop = await names()

// --- price filter (reset search first)
await page.click('button::-p-text(Reset)')
await new Promise((r) => setTimeout(r, 300))
await page.type('input[name="minPrice"]', '200')
await new Promise((r) => setTimeout(r, 500))
const afterMinPrice = await names()

// --- in stock only
await page.click('button::-p-text(Reset)')
await new Promise((r) => setTimeout(r, 300))
await page.click('input[name="inStockOnly"]')
await new Promise((r) => setTimeout(r, 500))
const afterInStock = await names()

const countText = await page.$eval('.filters-count', (el) => el.innerText.trim())

console.log('BASELINE:', JSON.stringify(baseline))
console.log('SEARCH_Pro:', JSON.stringify(afterPro))
console.log('SEARCH_Laptop:', JSON.stringify(afterLaptop))
console.log('MIN_PRICE_200:', JSON.stringify(afterMinPrice))
console.log('IN_STOCK_ONLY:', JSON.stringify(afterInStock))
console.log('COUNT_LABEL:', countText)
console.log('API_CALLS_DURING_FILTERING:', apiCalls)
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok =
  baseline.length === 5 &&
  afterPro.length === 2 &&
  afterPro.includes('Laptop Pro 15') &&
  afterPro.includes('Wireless Earbuds Pro') &&
  afterLaptop.length === 1 &&
  afterLaptop[0] === 'Laptop Pro 15' &&
  afterMinPrice.length === 3 &&
  !afterMinPrice.includes('Wireless Earbuds Pro') &&
  !afterMinPrice.includes('Portable Bluetooth Speaker') &&
  afterInStock.length === 5 &&
  apiCalls === 0 &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
