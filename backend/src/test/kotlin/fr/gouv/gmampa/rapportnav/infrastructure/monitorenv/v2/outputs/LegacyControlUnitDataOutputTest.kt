package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.LegacyControlUnitDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.LegacyControlUnitResourceDataOutput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LegacyControlUnitDataOutputTest {

    @Nested
    inner class ToLegacyControlUnitEntity {

        @Test
        fun `should convert to LegacyControlUnitEntity with all fields`() {
            val resources = listOf(
                LegacyControlUnitResourceDataOutput(id = 1, name = "PAM Themis"),
                LegacyControlUnitResourceDataOutput(id = 2, name = "PAM Iris")
            )

            val output = LegacyControlUnitDataOutput(
                id = 10,
                administration = "DIRM NAMO",
                isArchived = false,
                name = "Unite Cotiere",
                resources = resources
            )

            val result = output.toLegacyControlUnitEntity()

            assertEquals(10, result.id)
            assertEquals("DIRM NAMO", result.administration)
            assertEquals(false, result.isArchived)
            assertEquals("Unite Cotiere", result.name)
            assertEquals(2, result.resources?.size)
        }

        @Test
        fun `should map resources with control unit id`() {
            val resources = listOf(
                LegacyControlUnitResourceDataOutput(id = 1, name = "Resource 1"),
                LegacyControlUnitResourceDataOutput(id = 2, name = "Resource 2")
            )

            val output = LegacyControlUnitDataOutput(
                id = 42,
                administration = "Admin",
                isArchived = false,
                name = "Unit",
                resources = resources
            )

            val result = output.toLegacyControlUnitEntity()

            result.resources?.forEach { resource ->
                assertEquals(42, resource.controlUnitId)
            }
        }

        @Test
        fun `should handle empty resources list`() {
            val output = LegacyControlUnitDataOutput(
                id = 10,
                administration = "Admin",
                isArchived = false,
                name = "Unit",
                resources = emptyList()
            )

            val result = output.toLegacyControlUnitEntity()

            assertTrue(result.resources?.isEmpty() ?: true)
        }

        @Test
        fun `should handle archived control unit`() {
            val output = LegacyControlUnitDataOutput(
                id = 10,
                administration = "Admin",
                isArchived = true,
                name = "Archived Unit",
                resources = emptyList()
            )

            val result = output.toLegacyControlUnitEntity()

            assertEquals(true, result.isArchived)
        }

        @Test
        fun `should preserve resource names`() {
            val resources = listOf(
                LegacyControlUnitResourceDataOutput(id = 1, name = "PAM Themis"),
                LegacyControlUnitResourceDataOutput(id = 2, name = "Vedette VN 123")
            )

            val output = LegacyControlUnitDataOutput(
                id = 10,
                administration = "Admin",
                isArchived = false,
                name = "Unit",
                resources = resources
            )

            val result = output.toLegacyControlUnitEntity()

            val resourceNames = result.resources?.map { it.name } ?: emptyList()
            assertTrue(resourceNames.contains("PAM Themis"))
            assertTrue(resourceNames.contains("Vedette VN 123"))
        }

        @Test
        fun `should preserve resource ids`() {
            val resources = listOf(
                LegacyControlUnitResourceDataOutput(id = 101, name = "Resource A"),
                LegacyControlUnitResourceDataOutput(id = 202, name = "Resource B")
            )

            val output = LegacyControlUnitDataOutput(
                id = 10,
                administration = "Admin",
                isArchived = false,
                name = "Unit",
                resources = resources
            )

            val result = output.toLegacyControlUnitEntity()

            val resourceIds = result.resources?.map { it.id } ?: emptyList()
            assertTrue(resourceIds.contains(101))
            assertTrue(resourceIds.contains(202))
        }
    }
}
