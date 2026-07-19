/**
 * Clicks "Add to cart" in a real browser and asserts the cart table updates.
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

const cartRowsBefore = await page.$$eval('table', (tables) => {
  const cart = tables[tables.length - 1]
  return cart.querySelectorAll('tbody tr').length
})

// First "Add to cart" button in the products table.
const clicked = await page.evaluate(() => {
  const btn = [...document.querySelectorAll('button')].find(
    (b) => b.innerText.trim() === 'Add to cart' && !b.disabled,
  )
  if (!btn) return null
  const row = btn.closest('tr')
  const name = row.querySelectorAll('td')[1].innerText.trim()
  btn.click()
  return name
})

await new Promise((r) => setTimeout(r, 2500))

const result = await page.evaluate(() => {
  const tables = [...document.querySelectorAll('table')]
  const cart = tables[tables.length - 1]
  const rows = [...cart.querySelectorAll('tbody tr')].map((tr) =>
    [...tr.querySelectorAll('td')].map((td) => td.innerText.trim()),
  )
  return { rows, bodyText: document.body.innerText }
})

console.log('CLICKED_PRODUCT:', clicked)
console.log('CART_ROWS_BEFORE:', cartRowsBefore)
console.log('CART_ROWS_AFTER:', result.rows.length)
console.log('CART_CONTENTS:', JSON.stringify(result.rows))
console.log('SUCCESS_BANNER:', result.bodyText.includes('Added to cart.'))
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok = clicked && result.rows.length > 0 && errors.length === 0
console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
