/**
 * Verifies the loading spinner appears during a slow request, and that the
 * error banner + Retry appear when the backend request fails.
 */
import puppeteer from 'puppeteer-core'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})

// --- 1. Spinner: delay the products response and look for it mid-flight.
const slowPage = await browser.newPage()
await slowPage.setRequestInterception(true)
slowPage.on('request', async (req) => {
  if (req.url().includes('/api/products')) {
    await new Promise((r) => setTimeout(r, 2500))
    return req.continue()
  }
  return req.continue()
})

slowPage.goto('http://localhost:3000', { waitUntil: 'domcontentloaded' })
await new Promise((r) => setTimeout(r, 1200))

const spinnerVisible = await slowPage.evaluate(() => {
  const el = document.querySelector('.spinner')
  const status = document.querySelector('[role="status"]')
  return {
    hasSpinner: !!el,
    statusText: status?.innerText?.trim() ?? null,
  }
})
console.log('SPINNER_DURING_LOAD:', JSON.stringify(spinnerVisible))
await slowPage.close()

// --- 2. Error banner: fail the products request outright.
const failPage = await browser.newPage()
await failPage.setRequestInterception(true)
failPage.on('request', (req) => {
  if (req.url().includes('/api/products')) return req.abort('failed')
  return req.continue()
})

await failPage.goto('http://localhost:3000', { waitUntil: 'domcontentloaded' })
await new Promise((r) => setTimeout(r, 2500))

const errorState = await failPage.evaluate(() => {
  const banner = document.querySelector('[role="alert"]')
  const retry = [...document.querySelectorAll('button')].find(
    (b) => b.innerText.trim() === 'Retry',
  )
  return {
    hasBanner: !!banner,
    bannerText: banner?.innerText?.trim() ?? null,
    hasRetry: !!retry,
  }
})
console.log('ERROR_STATE:', JSON.stringify(errorState))
await failPage.close()

await browser.close()

const ok =
  spinnerVisible.hasSpinner &&
  errorState.hasBanner &&
  errorState.hasRetry &&
  /server|reach/i.test(errorState.bannerText ?? '')

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
