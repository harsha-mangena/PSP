import puppeteer from 'puppeteer-core'
import { seedSession, fetchSession } from './lib/session.mjs'

const sleep = (ms) => new Promise((r) => setTimeout(r, ms))
const session = await fetchSession()

const browser = await puppeteer.launch({
  executablePath: '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
  headless: 'new',
  args: ['--no-sandbox'],
  defaultViewport: { width: 1440, height: 1000 },
})
const page = await browser.newPage()
await seedSession(page, session)

const errors = []
page.on('pageerror', (e) => errors.push(e.message))
page.on('console', (m) => m.type() === 'error' && errors.push(m.text()))

await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await sleep(1500)

// Force all lazy-loaded product images to fetch before the full-page shot.
await page.evaluate(async () => {
  window.scrollTo(0, document.body.scrollHeight)
  await new Promise((r) => setTimeout(r, 400))
  window.scrollTo(0, 0)
})
await sleep(2500)

await page.screenshot({ path: '/tmp/bazaario-home.png', fullPage: true })
console.log('CONSOLE_ERRORS:', errors.length ? errors : 'none')

// Also grab a category-drilldown view
await page.evaluate(() => {
  document.querySelectorAll('.category-pill')[1]?.click()
})
await sleep(1200)
await page.screenshot({ path: '/tmp/bazaario-category.png', fullPage: true })

// And the cart with an item in it
await page.evaluate(() => {
  document.querySelector('.product-card-actions button.primary')?.click()
})
await sleep(1500)
await page.goto('http://localhost:3000/cart', { waitUntil: 'networkidle0' })
await sleep(1000)
await page.screenshot({ path: '/tmp/bazaario-cart.png', fullPage: true })

console.log('SCREENSHOTS_SAVED')
await browser.close()
