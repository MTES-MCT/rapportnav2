import { test, expect } from '@playwright/test'
import { createTestJwt } from '../../helpers/jwt'
import { mockApiCatchAll } from '../../helpers/mock-api'

/** Set up all post-login API mocks needed for the redirect chain to complete.
 *  Catch-all registered FIRST so that more specific routes (registered after) take precedence. */
async function mockPostLoginApis(page: import('@playwright/test').Page, user: Record<string, unknown>) {
  await mockApiCatchAll(page)
  await page.route('**/api/v2/users/*', (route) =>
    route.fulfill({ contentType: 'application/json', body: JSON.stringify(user) })
  )
}

test.describe('Login redirect chain', () => {
  test('PAM user: login -> / -> /pam/missions', async ({ page }) => {
    const token = createTestJwt({ userId: 1, roles: ['USER_PAM'] })

    await mockPostLoginApis(page, {
      id: 1, email: 'pam@test.gouv.fr', firstName: 'Jean', lastName: 'Dupont', serviceId: 10
    })
    await page.route('**/api/v1/auth/login', (route) =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify({ token }) })
    )

    await page.goto('/login', { waitUntil: 'networkidle' })
    await page.getByLabel('Email').fill('pam@test.gouv.fr')
    await page.getByLabel('Mot de passe').fill('password123')
    await page.getByRole('button', { name: 'Se connecter' }).click()

    await expect(page).toHaveURL(/\/pam\/missions/, { timeout: 10000 })
    await expect(page.getByText('Mes rapports')).toBeVisible()
  })

  test('ULAM user: login -> / -> /ulam/missions', async ({ page }) => {
    const token = createTestJwt({ userId: 2, roles: ['USER_ULAM'] })

    await mockPostLoginApis(page, {
      id: 2, email: 'ulam@test.gouv.fr', firstName: 'Marie', lastName: 'Martin', serviceId: 20
    })
    await page.route('**/api/v1/auth/login', (route) =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify({ token }) })
    )

    await page.goto('/login', { waitUntil: 'networkidle' })
    await page.getByLabel('Email').fill('ulam@test.gouv.fr')
    await page.getByLabel('Mot de passe').fill('password123')
    await page.getByRole('button', { name: 'Se connecter' }).click()

    await expect(page).toHaveURL(/\/ulam\/missions/, { timeout: 10000 })
    await expect(page.getByText('Mes rapports journaliers')).toBeVisible()
  })
})
