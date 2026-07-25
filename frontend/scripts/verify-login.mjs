/**
 * Verifies the login gate: protected routes redirect when signed out, wrong
 * credentials are rejected, correct ones grant access, the session survives a
 * reload, and sign-out revokes it.
 */
import puppeteer from 'puppeteer-core'
import { CHROME, CREDENTIALS } from './lib/session.mjs'

const sleep = (ms) => new Promise((r) => setTimeout(r, ms))

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})
const page = await browser.newPage()
const errors = []
page.on('pageerror', (e) => errors.push(e.message))

// --- 1. Signed out: a protected route bounces to /login
await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await sleep(700)
const redirectedUrl = page.url()
const hasNavWhenLoggedOut = await page.evaluate(() => !!document.querySelector('.site-header'))

// --- 2. Wrong credentials
await page.type('input[name="username"]', 'root')
await page.type('input[name="password"]', 'wrongpass')
await page.click('button[type="submit"]')
await sleep(1200)
const badLoginError = await page.evaluate(() => {
  const el = document.querySelector('[role="alert"]')
  return el ? el.innerText.trim() : null
})
const stillOnLogin = page.url().endsWith('/login')

// --- 3. Correct credentials
await page.$eval('input[name="password"]', (el) => {
  el.value = ''
  el.dispatchEvent(new Event('input', { bubbles: true }))
})
await page.type('input[name="password"]', CREDENTIALS.password)
await page.click('button[type="submit"]')
await sleep(2000)

const afterLoginUrl = page.url()
const navUser = await page.evaluate(() => {
  const el = document.querySelector('.user-chip')
  return el ? el.innerText.replace(/\n+/g, ' ').trim() : null
})
await sleep(1200)
const productRows = await page.$$eval('.product-card', (rows) => rows.length)

// --- 4. Session survives a reload
await page.reload({ waitUntil: 'networkidle0' })
await sleep(1500)
const urlAfterReload = page.url()
const stillSignedIn = await page.evaluate(() => !!document.querySelector('.user-chip'))

// --- 5. Sign out
await page.evaluate(() => {
  ;[...document.querySelectorAll('button')]
    .find((b) => b.innerText.trim() === 'Sign out')
    ?.click()
})
await sleep(1500)
const urlAfterSignOut = page.url()
const tokenCleared = await page.evaluate(() => localStorage.getItem('psp.auth') === null)

// --- 6. Back button must not restore access
await page.goto('http://localhost:3000/orders', { waitUntil: 'networkidle0' })
await sleep(800)
const urlAfterRetry = page.url()

console.log('LOGGED_OUT_REDIRECT:', redirectedUrl)
console.log('NAV_HIDDEN_WHEN_LOGGED_OUT:', !hasNavWhenLoggedOut)
console.log('BAD_LOGIN_ERROR:', badLoginError, '| stayed on login:', stillOnLogin)
console.log('AFTER_LOGIN_URL:', afterLoginUrl)
console.log('NAV_USER:', navUser)
console.log('PRODUCT_ROWS_VISIBLE:', productRows)
console.log('URL_AFTER_RELOAD:', urlAfterReload, '| still signed in:', stillSignedIn)
console.log('URL_AFTER_SIGNOUT:', urlAfterSignOut, '| token cleared:', tokenCleared)
console.log('PROTECTED_ROUTE_AFTER_SIGNOUT:', urlAfterRetry)
console.log('ERRORS:', errors.length ? errors : 'none')

await browser.close()

const ok =
  redirectedUrl.endsWith('/login') &&
  !hasNavWhenLoggedOut &&
  /Invalid username or password/.test(badLoginError ?? '') &&
  stillOnLogin &&
  afterLoginUrl.endsWith('/products') &&
  /root/.test(navUser ?? '') &&
  productRows > 0 &&
  urlAfterReload.endsWith('/products') &&
  stillSignedIn &&
  urlAfterSignOut.endsWith('/login') &&
  tokenCleared &&
  urlAfterRetry.endsWith('/login') &&
  errors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
