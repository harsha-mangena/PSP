/**
 * Verifies pagination is backend-driven: Next/Prev and page numbers each issue
 * a /api/products/paged request and swap the rendered rows.
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

await page.goto('http://localhost:3000', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 800))

const ids = () =>
  page.$$eval('table', (tables) =>
    [...tables[0].querySelectorAll('tbody tr')].map((tr) =>
      tr.querySelectorAll('td')[0].innerText.trim(),
    ),
  )

const info = () => page.$eval('.pagination-info', (el) => el.innerText.trim())

const page1 = await ids()
const info1 = await info()

await page.click('button::-p-text(Next)')
await new Promise((r) => setTimeout(r, 900))
const page2 = await ids()
const info2 = await info()

await page.click('button::-p-text(Prev)')
await new Promise((r) => setTimeout(r, 900))
const backTo1 = await ids()

// Sorting: price high -> low, should restart at page 1
await page.select('.sort-control select', 'price:desc')
await new Promise((r) => setTimeout(r, 900))
const sortedNames = await page.$$eval('table', (tables) =>
  [...tables[0].querySelectorAll('tbody tr')].map((tr) =>
    tr.querySelectorAll('td')[1].innerText.trim(),
  ),
)

console.log('PAGE_1_IDS:', JSON.stringify(page1), '|', info1)
console.log('PAGE_2_IDS:', JSON.stringify(page2), '|', info2)
console.log('BACK_TO_1_IDS:', JSON.stringify(backTo1))
console.log('SORTED_PRICE_DESC:', JSON.stringify(sortedNames))
console.log('PAGED_REQUESTS:', JSON.stringify(pagedRequests))
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const disjoint = page1.every((id) => !page2.includes(id))
const ok =
  page1.length > 0 &&
  page2.length > 0 &&
  disjoint &&
  JSON.stringify(backTo1) === JSON.stringify(page1) &&
  pagedRequests.length >= 4 &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
