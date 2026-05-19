import { test, expect } from '../../fixtures/auth'
import { mockMissionList } from '../../helpers/mock-api'
import missionDetail from '../../fixtures/data/mission-detail-ulam.json' with { type: 'json' }

test.describe('ULAM Create Mission', () => {
  test('should open create dialog with conditional form fields', async ({ ulamPage: page }) => {
    await mockMissionList(page, [])

    await page.goto('/ulam/missions', { waitUntil: 'networkidle' })
    await expect(page.getByText('Mes rapports journaliers')).toBeVisible({ timeout: 10000 })

    // Open create dialog
    await page.getByRole('button', { name: /Créer un rapport de mission/ }).click()
    await expect(page.getByText("Création d'un rapport")).toBeVisible({ timeout: 5000 })

    // Select "Rapport avec sortie terrain" → should show mission type checkboxes
    await page.getByText('Sélectionner').first().click()
    await page.getByText('Rapport avec sortie terrain').click()

    // Verify conditional fields appeared
    await expect(page.getByText('Type de mission', { exact: true })).toBeVisible()

    // Close dialog
    await page.locator('[data-testid="close-create-mission-icon"]').click()
    await expect(page.getByText("Création d'un rapport")).not.toBeVisible()
  })

  test('should load mission detail page', async ({ ulamPage: page }) => {
    await page.route('**/api/v2/missions/201', (route) =>
      route.fulfill({ contentType: 'application/json', body: JSON.stringify(missionDetail) })
    )

    await page.goto('/ulam/missions/201', { waitUntil: 'networkidle' })

    // Verify the 3 main sections of mission detail
    await expect(page.getByText('Informations générales')).toBeVisible({ timeout: 10000 })
    await expect(page.getByText('Actions réalisées en mission')).toBeVisible()
  })
})
