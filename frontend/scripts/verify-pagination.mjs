/**
 * Verifies pagination is backend-driven: Next/Prev and page-size changes each
 * issue a /api/products/paged request and swap the rendered cards. Pagination
 * only renders once a category is selected, and each seeded category has
 * just 5 products, so this drops the page size to 2 to get multiple pages.
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
const pagedRequests = []
page.on('pageerror', (e) => errors.push(e.message))
page.on('console', (m) => m.type() === 'error' && errors.push(m.text()))
page.on('request', (req) => {
  if (req.url().includes('/api/products/paged')) {
    pagedRequests.push(req.url().split('?')[1])
  }
})

await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 900))

await page.evaluate(() => {
  const pill = [...document.querySelectorAll('.category-pill')].find(
    (el) => el.innerText.trim() === 'Electronics',
  )
  pill?.click()
})
await new Promise((r) => setTimeout(r, 900))

await page.select('.pagination-size select', '2')
await new Promise((r) => setTimeout(r, 900))

const names = () =>
  page.$$eval('.product-card [data-testid="product-name"]', (els) =>
    els.map((el) => el.innerText.trim()),
  )

const info = () => page.$eval('.pagination-info', (el) => el.innerText.trim())

const page1 = await names()
const info1 = await info()

await page.click('button::-p-text(Next)')
await new Promise((r) => setTimeout(r, 900))
const page2 = await names()
const info2 = await info()

await page.click('button::-p-text(Prev)')
await new Promise((r) => setTimeout(r, 900))
const backTo1 = await names()

// Sorting: price high -> low, should restart at page 1
await page.select('.sort-control select', 'price:desc')
await new Promise((r) => setTimeout(r, 900))
const sortedNames = await names()

console.log('PAGE_1:', JSON.stringify(page1), '|', info1)
console.log('PAGE_2:', JSON.stringify(page2), '|', info2)
console.log('BACK_TO_1:', JSON.stringify(backTo1))
console.log('SORTED_PRICE_DESC:', JSON.stringify(sortedNames))
console.log('PAGED_REQUESTS:', JSON.stringify(pagedRequests))
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const disjoint = page1.every((name) => !page2.includes(name))
const ok =
  page1.length === 2 &&
  page2.length === 2 &&
  disjoint &&
  JSON.stringify(backTo1) === JSON.stringify(page1) &&
  sortedNames[0] === 'Laptop Pro 15' &&
  pagedRequests.length >= 4 &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
