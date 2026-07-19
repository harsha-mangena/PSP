/**
 * Regression checks for the reported "correct credentials still rejected" case:
 *  1. A stale error banner clears as soon as the user edits a field
 *  2. Correcting a wrong password in place then submitting succeeds
 *  3. Credentials pasted with stray whitespace still work
 *  4. Browser-autofilled values (set without firing React onChange) still submit
 */
import puppeteer from 'puppeteer-core'
import { CHROME } from './lib/session.mjs'

const sleep = (ms) => new Promise((r) => setTimeout(r, ms))
const results = {}

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})

// localStorage is shared across pages on the same origin, so each case must
// start from a genuinely signed-out state.
const freshPage = async () => {
  const page = await browser.newPage()
  await page.goto('http://localhost:3000/login', { waitUntil: 'domcontentloaded' })
  await page.evaluate(() => localStorage.clear())
  await page.goto('http://localhost:3000/login', { waitUntil: 'networkidle0' })
  await sleep(500)
  return page
}

const bannerText = (page) =>
  page.evaluate(() => {
    const el = document.querySelector('[role="alert"]')
    return el ? el.innerText.trim() : null
  })

// --- 1 & 2: fail, then correct in place
{
  const page = await freshPage()
  await page.type('input[name="username"]', 'root')
  await page.type('input[name="password"]', 'wrong')
  await page.click('button[type="submit"]')
  await sleep(1200)
  results.errorAfterBadLogin = await bannerText(page)

  // Edit the field -> banner should disappear immediately
  await page.type('input[name="password"]', 'x')
  await sleep(300)
  results.bannerClearedOnEdit = (await bannerText(page)) === null

  // Now correct it properly and submit
  await page.$eval('input[name="password"]', (el) => {
    el.value = ''
    el.dispatchEvent(new Event('input', { bubbles: true }))
  })
  await page.type('input[name="password"]', 'root1234')
  await page.click('button[type="submit"]')
  await sleep(2000)
  results.recoveredUrl = page.url()
  await page.close()
}

// --- 3: pasted whitespace
{
  const page = await freshPage()
  await page.type('input[name="username"]', ' root ')
  await page.type('input[name="password"]', 'root1234 ')
  await page.click('button[type="submit"]')
  await sleep(2000)
  results.whitespaceUrl = page.url()
  await page.close()
}

// --- 4: simulate browser autofill (sets .value without React onChange)
{
  const page = await freshPage()
  await page.evaluate(() => {
    document.querySelector('input[name="username"]').value = 'root'
    document.querySelector('input[name="password"]').value = 'root1234'
  })
  await page.click('button[type="submit"]')
  await sleep(2000)
  results.autofillUrl = page.url()
  await page.close()
}

// --- 5: the "Fill for me" button
{
  const page = await freshPage()
  await page.evaluate(() => {
    ;[...document.querySelectorAll('button')]
      .find((b) => b.innerText.trim() === 'Fill for me')
      ?.click()
  })
  await sleep(300)
  await page.click('button[type="submit"]')
  await sleep(2000)
  results.fillButtonUrl = page.url()
  await page.close()
}

await browser.close()

console.log('1. ERROR_AFTER_BAD_LOGIN :', results.errorAfterBadLogin)
console.log('2. BANNER_CLEARED_ON_EDIT:', results.bannerClearedOnEdit)
console.log('3. RECOVERED_AFTER_FIX   :', results.recoveredUrl)
console.log('4. PASTED_WHITESPACE     :', results.whitespaceUrl)
console.log('5. BROWSER_AUTOFILL      :', results.autofillUrl)
console.log('6. FILL_FOR_ME_BUTTON    :', results.fillButtonUrl)

const ok =
  /Invalid username or password/.test(results.errorAfterBadLogin ?? '') &&
  results.bannerClearedOnEdit &&
  results.recoveredUrl.endsWith('/products') &&
  results.whitespaceUrl.endsWith('/products') &&
  results.autofillUrl.endsWith('/products') &&
  results.fillButtonUrl.endsWith('/products')

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
