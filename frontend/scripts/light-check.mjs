import puppeteer from 'puppeteer-core'
import { seedSession, fetchSession } from './lib/session.mjs'

const session = await fetchSession()
const browser = await puppeteer.launch({
  executablePath: '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
  headless: 'new',
  args: ['--no-sandbox'],
  defaultViewport: { width: 1440, height: 1000 },
})
const page = await browser.newPage()
await page.emulateMediaFeatures([{ name: 'prefers-color-scheme', value: 'light' }])
await seedSession(page, session)
await page.goto('http://localhost:3000/products', { waitUntil: 'networkidle0' })
await new Promise((r) => setTimeout(r, 1200))
await page.screenshot({ path: '/tmp/bazaario-light.png' })
await browser.close()
