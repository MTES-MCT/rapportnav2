import { Page } from '@playwright/test'
import userPam from '../fixtures/data/user-pam.json' with { type: 'json' }
import userUlam from '../fixtures/data/user-ulam.json' with { type: 'json' }

/**
 * Mock all static data endpoints that the app prefetches via usePrefetchStaticData.
 * Uses the actual endpoint names from the source code (agent_roles with underscore, etc.).
 */
export async function mockStaticData(page: Page) {
  const staticEndpoints = [
    '**/api/v2/agents',
    '**/api/v2/agent_roles',
    '**/api/v2/administrations',
    '**/api/v2/resources',
    '**/api/v2/natinfs',
    '**/api/v2/ports',
    '**/api/v2/fish-auctions'
  ]
  for (const pattern of staticEndpoints) {
    await page.route(pattern, (route) => route.fulfill({ body: '[]', contentType: 'application/json' }))
  }
}

/** Mock the user endpoint for a PAM user */
export async function mockPamUser(page: Page) {
  await page.route('**/api/v2/users/*', (route) =>
    route.fulfill({ body: JSON.stringify(userPam), contentType: 'application/json' })
  )
}

/** Mock the user endpoint for a ULAM user */
export async function mockUlamUser(page: Page) {
  await page.route('**/api/v2/users/*', (route) =>
    route.fulfill({ body: JSON.stringify(userUlam), contentType: 'application/json' })
  )
}

/** Mock the user endpoint for an admin user */
export async function mockAdminUser(page: Page) {
  await page.route('**/api/v2/users/*', (route) =>
    route.fulfill({
      contentType: 'application/json',
      body: JSON.stringify({ id: 3, email: 'admin@test.gouv.fr', firstName: 'Admin', lastName: 'User', serviceId: 30 })
    })
  )
}

/** Mock the missions list endpoint */
export async function mockMissionList(page: Page, missions: unknown[] = []) {
  await page.route('**/api/v2/missions?**', (route) =>
    route.fulfill({ body: JSON.stringify(missions), contentType: 'application/json' })
  )
}

/** Mock the inquiries list endpoint */
export async function mockInquiryList(page: Page, inquiries: unknown[] = []) {
  await page.route('**/api/v2/inquiries?**', (route) =>
    route.fulfill({ body: JSON.stringify(inquiries), contentType: 'application/json' })
  )
}

/** Mock POST for mission creation (ULAM) */
export async function mockCreateMission(page: Page, response: Record<string, unknown> = { id: 999 }) {
  await page.route('**/api/v2/missions', (route) => {
    if (route.request().method() === 'POST') {
      return route.fulfill({ body: JSON.stringify(response), contentType: 'application/json', status: 201 })
    }
    return route.continue()
  })
}

/**
 * Catch-all for any /api/ request not already handled by a more specific route.
 * Logs the URL to help debug missing mocks, then returns an empty array.
 * Must be registered LAST (Playwright matches routes in reverse registration order).
 */
export async function mockApiCatchAll(page: Page) {
  await page.route('**/api/**', (route) => {
    if (process.env.DEBUG_MOCKS) {
      console.warn(`[e2e] Unmocked API call: ${route.request().method()} ${route.request().url()}`)
    }
    route.fulfill({ body: '[]', contentType: 'application/json' })
  })
}

/**
 * Clear React Query persisted cache to prevent stale data between tests.
 * Must be called before navigating to any page.
 */
export async function clearQueryCache(page: Page) {
  await page.addInitScript(() => {
    const keys = Object.keys(localStorage)
    for (const key of keys) {
      if (key.startsWith('REACT_QUERY')) {
        localStorage.removeItem(key)
      }
    }
  })
}
