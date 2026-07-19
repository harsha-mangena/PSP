/**
 * Drives the Add Product form in a real browser and asserts the new row shows
 * up in the list.
 */
import puppeteer from 'puppeteer-core'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
const name = `UITest-${Date.now()}`

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

const before = await page.$$eval('tbody tr', (rows) => rows.length)

await page.type('input[name="name"]', name)
await page.type('input[name="price"]', '12.34')
await page.type('input[name="stock"]', '77')
await page.click('button[type="submit"]')

await new Promise((r) => setTimeout(r, 2000))

const after = await page.$$eval('tbody tr', (rows) => rows.length)
const bodyText = await page.evaluate(() => document.body.innerText)

console.log('ROWS_BEFORE:', before)
console.log('ROWS_AFTER:', after)
console.log('NEW_PRODUCT_IN_TABLE:', bodyText.includes(name))
console.log('SUCCESS_MESSAGE:', /Created "UITest-\d+" \(id \d+\)/.test(bodyText))
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok = after === before + 1 && bodyText.includes(name) && errors.length === 0
console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
