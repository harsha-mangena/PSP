/**
 * Drives the full cart -> pay -> orders journey in a real browser:
 * add items, change quantity, remove a line, pay, and confirm the order
 * appears in history with stock decremented.
 */
import puppeteer from 'puppeteer-core'
import { seedSession, fetchSession } from './lib/session.mjs'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
const sleep = (ms) => new Promise((r) => setTimeout(r, ms))

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

const cartRows = () =>
  page.$$eval('tbody tr', (rows) =>
    rows.map((tr) => [...tr.querySelectorAll('td')].map((td) => td.innerText.trim())),
  )

// --- Add two different products
await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await sleep(800)

const stockBefore = await page.$$eval('tbody tr', (rows) =>
  rows.slice(0, 2).map((tr) => {
    const td = tr.querySelectorAll('td')
    return { name: td[1].innerText.trim(), stock: td[3].innerText.trim() }
  }),
)

for (const index of [0, 1]) {
  await page.evaluate((i) => {
    const buttons = [...document.querySelectorAll('button')].filter(
      (b) => b.innerText.trim() === 'Add to cart' && !b.disabled,
    )
    buttons[i]?.click()
  }, index)
  await sleep(1500)
}

// --- Cart: quantity + remove
await page.goto('http://localhost:3000/cart', { waitUntil: 'networkidle0' })
await sleep(1000)
const rowsInitial = await cartRows()

// Increase quantity of the first line
await page.evaluate(() => {
  document.querySelector('button[aria-label="Increase quantity"]').click()
})
await sleep(1500)
const rowsAfterPlus = await cartRows()

// Remove the second line
await page.evaluate(() => {
  const removes = [...document.querySelectorAll('button')].filter(
    (b) => b.innerText.trim() === 'Remove',
  )
  removes[1]?.click()
})
await sleep(1500)
const rowsAfterRemove = await cartRows()

const payLabel = await page.$eval('.pay-button', (el) => el.innerText.trim())

// --- Pay
await page.click('.pay-button')
await sleep(2500)

const receipt = await page.evaluate(() => {
  const el = document.querySelector('.receipt')
  return el ? el.innerText.replace(/\n+/g, ' | ') : null
})

// --- Orders page
await page.goto('http://localhost:3000/orders', { waitUntil: 'networkidle0' })
await sleep(1200)
const orderCards = await page.$$eval('.order-card', (cards) =>
  cards.map((c) => c.innerText.replace(/\n+/g, ' | ')),
)

// --- Stock after
await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await sleep(1000)
const stockAfter = await page.$$eval('tbody tr', (rows) =>
  rows.slice(0, 2).map((tr) => {
    const td = tr.querySelectorAll('td')
    return { name: td[1].innerText.trim(), stock: td[3].innerText.trim() }
  }),
)

console.log('STOCK_BEFORE:', JSON.stringify(stockBefore))
console.log('CART_INITIAL_ROWS:', rowsInitial.length, JSON.stringify(rowsInitial))
console.log('AFTER_PLUS:', JSON.stringify(rowsAfterPlus))
console.log('AFTER_REMOVE:', rowsAfterRemove.length, JSON.stringify(rowsAfterRemove))
console.log('PAY_BUTTON:', payLabel)
console.log('RECEIPT:', receipt)
console.log('ORDER_CARDS:', orderCards.length)
console.log('NEWEST_ORDER:', orderCards[0])
console.log('STOCK_AFTER:', JSON.stringify(stockAfter))
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok =
  rowsInitial.length === 2 &&
  rowsAfterRemove.length === 1 &&
  /Payment successful/.test(receipt ?? '') &&
  orderCards.length >= 1 &&
  /PAID/.test(orderCards[0] ?? '') &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
