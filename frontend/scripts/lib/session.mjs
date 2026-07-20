/**
 * Shared test helper: obtains a real token through the API gateway and seeds it
 * into localStorage before the app boots, so protected routes render directly
 * and the axios interceptor has a token to attach.
 */
export const CHROME = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'

export const CREDENTIALS = { username: 'root', password: 'root1234' }

export async function fetchSession() {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(CREDENTIALS),
  })
  if (!response.ok) {
    throw new Error(`login failed: ${response.status}`)
  }
  return response.json()
}

/**
 * Must be called before page.goto so the store picks the session up on init.
 */
export async function seedSession(page, session) {
  await page.evaluateOnNewDocument((value) => {
    localStorage.setItem('psp.auth', JSON.stringify(value))
  }, session)
}

export async function authedPage(browser) {
  const session = await fetchSession()
  const page = await browser.newPage()
  await seedSession(page, session)
  return { page, session }
}
