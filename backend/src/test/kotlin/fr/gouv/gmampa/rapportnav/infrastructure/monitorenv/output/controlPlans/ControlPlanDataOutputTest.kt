package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.output.controlPlans

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.controlPlans.ControlPlanDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.controlPlans.ControlPlanSubThemeDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.controlPlans.ControlPlanTagDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.controlPlans.ControlPlanThemeDataOutput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ControlPlanDataOutputTest {

    @Nested
    inner class ToControlPlansEntity {

        @Test
        fun `should convert to ControlPlansEntity with all data`() {
            val themes = mapOf(
                1 to ControlPlanThemeDataOutput(id = 1, theme = "Police des mouillages"),
                2 to ControlPlanThemeDataOutput(id = 2, theme = "Police de l'eau")
            )

            val subThemes = mapOf(
                10 to ControlPlanSubThemeDataOutput(id = 10, themeId = 1, subTheme = "ZMEL", year = 2024),
                20 to ControlPlanSubThemeDataOutput(id = 20, themeId = 2, subTheme = "Qualite de l'eau", year = 2024)
            )

            val tags = mapOf(
                100 to ControlPlanTagDataOutput(id = 100, tag = "Urgent", themeId = 1),
                200 to ControlPlanTagDataOutput(id = 200, tag = "Priority", themeId = 2)
            )

            val output = ControlPlanDataOutput(
                themes = themes,
                subThemes = subThemes,
                tags = tags
            )

            val result = output.toControlPlansEntity()

            assertEquals(2, result.themes.size)
            assertEquals(2, result.subThemes.size)
            assertEquals(2, result.tags.size)
        }

        @Test
        fun `should map themes correctly`() {
            val themes = mapOf(
                1 to ControlPlanThemeDataOutput(id = 1, theme = "Theme A"),
                2 to ControlPlanThemeDataOutput(id = 2, theme = "Theme B")
            )

            val output = ControlPlanDataOutput(
                themes = themes,
                subThemes = emptyMap(),
                tags = emptyMap()
            )

            val result = output.toControlPlansEntity()

            val themeA = result.themes.find { it.id == 1 }
            val themeB = result.themes.find { it.id == 2 }

            assertNotNull(themeA)
            assertNotNull(themeB)
            assertEquals("Theme A", themeA?.theme)
            assertEquals("Theme B", themeB?.theme)
        }

        @Test
        fun `should map subThemes correctly`() {
            val subThemes = mapOf(
                10 to ControlPlanSubThemeDataOutput(id = 10, themeId = 1, subTheme = "SubTheme A", year = 2024),
                20 to ControlPlanSubThemeDataOutput(id = 20, themeId = 1, subTheme = "SubTheme B", year = 2023)
            )

            val output = ControlPlanDataOutput(
                themes = emptyMap(),
                subThemes = subThemes,
                tags = emptyMap()
            )

            val result = output.toControlPlansEntity()

            val subThemeA = result.subThemes.find { it.id == 10 }
            val subThemeB = result.subThemes.find { it.id == 20 }

            assertNotNull(subThemeA)
            assertNotNull(subThemeB)
            assertEquals("SubTheme A", subThemeA?.subTheme)
            assertEquals(1, subThemeA?.themeId)
            assertEquals(2024, subThemeA?.year)
            assertEquals("SubTheme B", subThemeB?.subTheme)
            assertEquals(2023, subThemeB?.year)
        }

        @Test
        fun `should map tags correctly`() {
            val tags = mapOf(
                100 to ControlPlanTagDataOutput(id = 100, tag = "Tag A", themeId = 1),
                200 to ControlPlanTagDataOutput(id = 200, tag = "Tag B", themeId = 2)
            )

            val output = ControlPlanDataOutput(
                themes = emptyMap(),
                subThemes = emptyMap(),
                tags = tags
            )

            val result = output.toControlPlansEntity()

            val tagA = result.tags.find { it.id == 100 }
            val tagB = result.tags.find { it.id == 200 }

            assertNotNull(tagA)
            assertNotNull(tagB)
            assertEquals("Tag A", tagA?.tag)
            assertEquals(1, tagA?.themeId)
            assertEquals("Tag B", tagB?.tag)
            assertEquals(2, tagB?.themeId)
        }

        @Test
        fun `should handle empty maps`() {
            val output = ControlPlanDataOutput(
                themes = emptyMap(),
                subThemes = emptyMap(),
                tags = emptyMap()
            )

            val result = output.toControlPlansEntity()

            assertTrue(result.themes.isEmpty())
            assertTrue(result.subThemes.isEmpty())
            assertTrue(result.tags.isEmpty())
        }

        @Test
        fun `should preserve map keys as entity ids`() {
            val themes = mapOf(
                5 to ControlPlanThemeDataOutput(id = 5, theme = "Theme with ID 5"),
                10 to ControlPlanThemeDataOutput(id = 10, theme = "Theme with ID 10")
            )

            val output = ControlPlanDataOutput(
                themes = themes,
                subThemes = emptyMap(),
                tags = emptyMap()
            )

            val result = output.toControlPlansEntity()

            val theme5 = result.themes.find { it.id == 5 }
            val theme10 = result.themes.find { it.id == 10 }

            assertNotNull(theme5)
            assertNotNull(theme10)
        }

        @Test
        fun `should handle large dataset`() {
            val themes = (1..100).associate { i ->
                i to ControlPlanThemeDataOutput(id = i, theme = "Theme $i")
            }

            val subThemes = (1..200).associate { i ->
                i to ControlPlanSubThemeDataOutput(id = i, themeId = i % 100 + 1, subTheme = "SubTheme $i", year = 2024)
            }

            val tags = (1..50).associate { i ->
                i to ControlPlanTagDataOutput(id = i, tag = "Tag $i", themeId = i % 100 + 1)
            }

            val output = ControlPlanDataOutput(
                themes = themes,
                subThemes = subThemes,
                tags = tags
            )

            val result = output.toControlPlansEntity()

            assertEquals(100, result.themes.size)
            assertEquals(200, result.subThemes.size)
            assertEquals(50, result.tags.size)
        }
    }
}
