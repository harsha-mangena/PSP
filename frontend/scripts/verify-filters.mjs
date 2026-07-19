/**
 * Exercises search + price filters and asserts the table narrows in real time
 * without any additional network requests (filtering is client-side/useMemo).
 */
import puppeteer from 'puppeteer-core'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})
const page = await browser.newPage()
const errors = []
page.on('pageerror', (e) => errors.push(e.message))
page.on('console', (m) => m.type() === 'error' && errors.push(m.text()))

await page.goto('http://localhost:3000', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 800))

const productRows = () =>
  page.$$eval('table', (tables) => tables[0].querySelectorAll('tbody tr').length)

const names = () =>
  page.$$eval('table', (tables) =>
    [...tables[0].querySelectorAll('tbody tr')].map(
      (tr) => tr.querySelectorAll('td')[1].innerText.trim(),
    ),
  )

const baseline = await productRows()

// Count network calls from here on, to prove filtering does not refetch.
let apiCalls = 0
page.on('request', (req) => {
  if (req.url().includes('/api/')) apiCalls += 1
})

// --- search
await page.type('input[name="search"]', 'o')
await new Promise((r) => setTimeout(r, 500))
const afterSearch = await names()

await page.$eval('input[name="search"]', (el) => {
  el.value = ''
  el.dispatchEvent(new Event('input', { bubbles: true }))
})
await page.type('input[name="search"]', 'keyb')
await new Promise((r) => setTimeout(r, 500))
const afterKeyb = await names()

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

console.log('BASELINE_ROWS:', baseline)
console.log('SEARCH_o:', JSON.stringify(afterSearch))
console.log('SEARCH_keyb:', JSON.stringify(afterKeyb))
console.log('MIN_PRICE_200:', JSON.stringify(afterMinPrice))
console.log('IN_STOCK_ONLY:', JSON.stringify(afterInStock))
console.log('COUNT_LABEL:', countText)
console.log('API_CALLS_DURING_FILTERING:', apiCalls)
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok =
  afterKeyb.length === 1 &&
  afterKeyb[0] === 'Keyboard' &&
  afterSearch.length < baseline &&
  afterMinPrice.every((n) => n !== 'Keyboard') &&
  !afterInStock.includes('Webcam') &&
  apiCalls === 0 &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
