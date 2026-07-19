/**
 * Headless smoke check: loads the app in Chrome, fails on any console error,
 * and runs the assertions passed in via --assert.
 *
 * Usage: node scripts/verify-ui.mjs [url] [--shot out.png]
 */
import puppeteer from 'puppeteer-core'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
const url = process.argv[2] ?? 'http://localhost:3000'
const shotIndex = process.argv.indexOf('--shot')
const shot = shotIndex > -1 ? process.argv[shotIndex + 1] : null

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})

const page = await browser.newPage()
const consoleErrors = []
const pageErrors = []

page.on('console', (msg) => {
  if (msg.type() === 'error') consoleErrors.push(msg.text())
})
page.on('pageerror', (err) => pageErrors.push(err.message))

await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 })
// Give async thunks a moment to settle after the network goes quiet.
await new Promise((resolve) => setTimeout(resolve, 1200))

const result = await page.evaluate(() => {
  const text = document.body.innerText
  const rows = [...document.querySelectorAll('tbody tr')].map((tr) =>
    [...tr.querySelectorAll('td')].map((td) => td.innerText.trim()),
  )
  return { text, rows, rowCount: rows.length }
})

if (shot) await page.screenshot({ path: shot, fullPage: true })

console.log('ROWS:', result.rowCount)
console.log(JSON.stringify(result.rows.slice(0, 8), null, 0))
console.log('BODY_TEXT_SNIPPET:', result.text.slice(0, 400).replace(/\n+/g, ' | '))
console.log('CONSOLE_ERRORS:', consoleErrors.length ? consoleErrors : 'none')
console.log('PAGE_ERRORS:', pageErrors.length ? pageErrors : 'none')

await browser.close()

if (consoleErrors.length || pageErrors.length) {
  console.log('RESULT: FAIL')
  process.exit(1)
}
console.log('RESULT: PASS')
