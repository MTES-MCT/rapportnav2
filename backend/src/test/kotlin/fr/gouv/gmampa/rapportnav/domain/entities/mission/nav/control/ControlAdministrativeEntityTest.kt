package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class ControlAdministrativeEntityTest {

    @Test
    fun `should return false when unitShouldConfirm is false`() {
        // Arrange
        val entity = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            actionControlId = "action-1",
            amountOfControls = 10,
            unitShouldConfirm = false, // Should make the method return false
            unitHasConfirmed = null
        )

        // Act
        val result = entity.shouldToggleOnUnitHasConfirmed()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `should return false when unitHasConfirmed is true`() {
        // Arrange
        val entity = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            actionControlId = "action-2",
            amountOfControls = 5,
            unitShouldConfirm = true,
            unitHasConfirmed = true // Should make the method return false
        )

        // Act
        val result = entity.shouldToggleOnUnitHasConfirmed()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `should return true when compliantOperatingPermit is not null`() {
        // Arrange
        val entity = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            actionControlId = "action-3",
            amountOfControls = 8,
            unitShouldConfirm = true,
            unitHasConfirmed = null,
            compliantOperatingPermit = ControlResult.NOT_CONCERNED // Non-null value
        )

        // Act
        val result = entity.shouldToggleOnUnitHasConfirmed()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `should return true when observations is not null`() {
        // Arrange
        val entity = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            actionControlId = "action-4",
            amountOfControls = 12,
            unitShouldConfirm = true,
            unitHasConfirmed = null,
            observations = "Some observations" // Non-null value
        )

        // Act
        val result = entity.shouldToggleOnUnitHasConfirmed()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `should return true when infractions is not empty`() {
        // Arrange
        val entity = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            actionControlId = "action-5",
            amountOfControls = 15,
            unitShouldConfirm = true,
            unitHasConfirmed = null,
            infractions = listOf(
                InfractionEntity(
                    id = UUID.randomUUID(),
                    missionId = "1",
                    actionId = "action-5",
                )
            )
        )

        // Act
        val result = entity.shouldToggleOnUnitHasConfirmed()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `should check if equals or not equals `() {
        val id = UUID.randomUUID()
        val entity1 = ControlAdministrativeEntity(
            id = id,
            missionId = "1",
            actionControlId = "action-5",
            amountOfControls = 15,
            unitShouldConfirm = true,
            unitHasConfirmed = null,
            compliantSecurityDocuments = ControlResult.YES,
            compliantOperatingPermit = ControlResult.NOT_CONCERNED,
            upToDateNavigationPermit = ControlResult.NOT_CONCERNED,
        )

        val entity2 = ControlAdministrativeEntity(
            id = id,
            missionId = "1",
            actionControlId = "action-5",
            amountOfControls = 15,
            unitShouldConfirm = true,
            unitHasConfirmed = null,
            compliantSecurityDocuments = ControlResult.YES,
            compliantOperatingPermit = ControlResult.NOT_CONTROLLED,
            upToDateNavigationPermit = ControlResult.NOT_CONCERNED,
            )
        // Assert
        assertFalse(entity1.equals(entity2))
    }
}
