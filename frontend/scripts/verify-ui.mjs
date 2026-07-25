/**
 * Headless smoke check: loads the storefront home in Chrome, fails on any
 * console error, and confirms every category section rendered real product
 * cards from the backend.
 */
import puppeteer from 'puppeteer-core'
import { seedSession, fetchSession } from './lib/session.mjs'

const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
const url = process.argv[2] ?? 'http://localhost:3000'

const session = await fetchSession()

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: 'new',
  args: ['--no-sandbox'],
})

const page = await browser.newPage()
await seedSession(page, session)
const consoleErrors = []
const pageErrors = []

page.on('console', (msg) => {
  if (msg.type() === 'error') consoleErrors.push(msg.text())
})
page.on('pageerror', (err) => pageErrors.push(err.message))

await page.goto(`${url}/products`, { waitUntil: 'networkidle0', timeout: 30000 })
await new Promise((resolve) => setTimeout(resolve, 1500))

const result = await page.evaluate(() => {
  const sections = [...document.querySelectorAll('.category-section')].map((section) => ({
    title: section.querySelector('.category-section-title')?.innerText.trim(),
    cardCount: section.querySelectorAll('.product-card').length,
  }))
  const firstCard = document.querySelector('.product-card')
  return {
    sectionCount: sections.length,
    sections,
    totalCards: document.querySelectorAll('.product-card').length,
    hasBanner: !!document.querySelector('.banner-carousel'),
    hasCategoryRail: !!document.querySelector('.category-rail'),
    firstCardName: firstCard?.querySelector('[data-testid="product-name"]')?.innerText.trim(),
    firstCardPrice: firstCard?.querySelector('[data-testid="product-price"]')?.innerText.trim(),
  }
})

console.log('SECTIONS:', result.sectionCount, JSON.stringify(result.sections))
console.log('TOTAL_CARDS:', result.totalCards)
console.log('HAS_BANNER:', result.hasBanner)
console.log('HAS_CATEGORY_RAIL:', result.hasCategoryRail)
console.log('FIRST_CARD:', result.firstCardName, result.firstCardPrice)
console.log('CONSOLE_ERRORS:', consoleErrors.length ? consoleErrors : 'none')
console.log('PAGE_ERRORS:', pageErrors.length ? pageErrors : 'none')

await browser.close()

const ok =
  result.sectionCount === 5 &&
  result.totalCards === 25 &&
  result.sections.every((s) => s.cardCount === 5) &&
  result.hasBanner &&
  result.hasCategoryRail &&
  !!result.firstCardName &&
  !!result.firstCardPrice &&
  consoleErrors.length === 0 &&
  pageErrors.length === 0

console.log('RESULT:', ok ? 'PASS' : 'FAIL')
process.exit(ok ? 0 : 1)
