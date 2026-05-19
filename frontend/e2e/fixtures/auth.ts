import { test as base, Page } from '@playwright/test'
import { createTestJwt } from '../helpers/jwt'
import { clearQueryCache, mockApiCatchAll, mockPamUser, mockStaticData, mockUlamUser } from '../helpers/mock-api'

type AuthFixtures = {
  pamPage: Page
  ulamPage: Page
}

async function createAuthContext(browser: any, userId: number, roles: string[]): Promise<{ context: any; page: Page }> {
  const context = await browser.newContext({
    storageState: {
      cookies: [{ name: 'XSRF-TOKEN', value: 'test-csrf', domain: 'localhost', path: '/' }],
      origins: [
        {
          origin: 'http://localhost:5173',
          localStorage: [{ name: 'jwt', value: createTestJwt({ userId, roles }) }]
        }
      ]
    }
  })
  const page = await context.newPage()
  await clearQueryCache(page)
  await mockStaticData(page)
  return { context, page }
}

export const test = base.extend<AuthFixtures>({
  pamPage: async ({ browser }, use) => {
    const { context, page } = await createAuthContext(browser, 1, ['USER_PAM'])
    await mockPamUser(page)
    await mockApiCatchAll(page)
    await use(page)
    await context.close()
  },

  ulamPage: async ({ browser }, use) => {
    const { context, page } = await createAuthContext(browser, 2, ['USER_ULAM'])
    await mockUlamUser(page)
    await mockApiCatchAll(page)
    await use(page)
    await context.close()
  }
})

export { expect } from '@playwright/test'
