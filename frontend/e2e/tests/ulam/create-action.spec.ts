import { test, expect } from '../../fixtures/auth'
import missionDetail from '../../fixtures/data/mission-detail-ulam.json' with { type: 'json' }

test.describe('ULAM Create Action on Mission Page', () => {
  test('should add a NOTE action and navigate to it', async ({ ulamPage: page }) => {
    const actionId = 'action-note-001'

    const noteAction = {
      id: actionId,
      actionType: 'NOTE',
      source: 'RAPPORT_NAV',
      data: {
        startDateTimeUtc: '2026-01-15T10:30:00Z',
        endDateTimeUtc: '2026-01-15T10:30:00Z',
        observations: '',
        isWithinDepartment: true
      },
      missionId: 201,
      controlsToComplete: [],
      completenessForStats: {},
      sourcesOfMissingDataForStats: []
    }

    // Mock GET /missions/{id} — starts with no actions
    await page.route('**/api/v2/missions/201', (route) =>
      route.fulfill({ contentType: 'application/json', body: JSON.stringify(missionDetail) })
    )

    // Mock POST /owners/{id}/actions — returns the created note action
    await page.route('**/api/v2/owners/*/actions', (route) => {
      if (route.request().method() === 'POST') {
        return route.fulfill({
          status: 201,
          contentType: 'application/json',
          body: JSON.stringify(noteAction)
        })
      }
      return route.continue()
    })

    // Mock GET /owners/{id}/actions/{actionId} — action detail
    await page.route('**/api/v2/owners/*/actions/*', (route) => {
      if (route.request().method() === 'GET') {
        return route.fulfill({
          contentType: 'application/json',
          body: JSON.stringify(noteAction)
        })
      }
      return route.continue()
    })

    // Navigate to mission detail page
    await page.goto('/ulam/missions/201', { waitUntil: 'networkidle' })

    // Verify timeline header is visible
    await expect(page.getByText('Actions réalisées en mission')).toBeVisible({ timeout: 10000 })

    // Click "Ajouter" dropdown button (exact match to avoid "Ajouter un moyen" etc.)
    await page.getByRole('button', { name: 'Ajouter', exact: true }).click()

    // The dropdown should open — click "Ajouter une note libre"
    await page.getByText('Ajouter une note libre').click()

    // Should navigate to the action URL (missionId/actionId)
    await expect(page).toHaveURL(/\/ulam\/missions\/201\//, { timeout: 10000 })
  })
})
