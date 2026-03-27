package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class RequiredFieldsValidatorTest {

    // =========================================================================
    // MissionGeneralInfoEntity tests
    // =========================================================================
    @Nested
    @DisplayName("MissionGeneralInfoEntity")
    inner class GeneralInfoTests {

        @Test
        fun `should be valid when all required fields are present`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                distanceInNauticalMiles = 50.5f,
                consumedGOInLiters = 100.0f,
                consumedFuelInLiters = 200.0f,
                nbrOfRecognizedVessel = 5
            )

            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertTrue(result.isComplete)
        }

        @Test
        fun `should be invalid when required fields are null`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                distanceInNauticalMiles = null,
                consumedGOInLiters = null,
                consumedFuelInLiters = null,
                nbrOfRecognizedVessel = null
            )

            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertFalse(result.isComplete)
            assertEquals(4, result.errors.size)
        }

        @Test
        fun `should not validate with ValidateThrowsBeforeSave group`() {
            val entity = MissionGeneralInfoEntity(
                distanceInNauticalMiles = null
            )

            val result = EntityValidityValidator.validateStatic(entity, ValidateThrowsBeforeSave::class.java)

            assertTrue(result.isComplete)
        }
    }

    // =========================================================================
    // MissionNavActionEntity tests
    // =========================================================================
    @Nested
    @DisplayName("MissionNavActionEntity")
    inner class NavActionTests {

        private fun createNavAction(
            actionType: ActionType = ActionType.OTHER,
            startDateTimeUtc: Instant? = Instant.now(),
            endDateTimeUtc: Instant? = Instant.now().plusSeconds(3600),
            latitude: Double? = null,
            longitude: Double? = null,
            controlMethod: ControlMethod? = null,
            vesselIdentifier: String? = null,
            vesselType: VesselTypeEnum? = null,
            vesselSize: VesselSizeEnum? = null,
            identityControlledPerson: String? = null,
            isPersonRescue: Boolean? = null,
            numberPersonsRescued: Int? = null,
            status: ActionStatusType? = null,
            reason: ActionStatusReason? = null,
            isMigrationRescue: Boolean? = null
        ) = MissionNavActionEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            actionType = actionType,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            latitude = latitude,
            longitude = longitude,
            controlMethod = controlMethod,
            vesselIdentifier = vesselIdentifier,
            vesselType = vesselType,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            isPersonRescue = isPersonRescue,
            numberPersonsRescued = numberPersonsRescued,
            status = status,
            reason = reason,
            isMigrationRescue = isMigrationRescue
        )

        @Test
        fun `should be valid when all always-required fields are present`() {
            val entity = createNavAction(actionType = ActionType.OTHER)
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertTrue(result.isComplete)
        }

        @Test
        fun `should be invalid when startDateTimeUtc is null`() {
            val entity = createNavAction(startDateTimeUtc = null)
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertFalse(result.isComplete)
            assertTrue(result.errors.any { it.field == "startDateTimeUtc" })
        }

        @Test
        fun `should require CONTROL-specific fields`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                controlMethod = null,
                latitude = null
            )
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertFalse(result.isComplete)
            assertTrue(result.errors.any { it.field == "controlMethod" })
            assertTrue(result.errors.any { it.field == "latitude" })
        }

        @Test
        fun `should be valid when CONTROL has all required fields`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                latitude = 48.8566,
                longitude = 2.3522,
                controlMethod = ControlMethod.SEA,
                vesselIdentifier = "ABC123",
                vesselType = VesselTypeEnum.FISHING,
                vesselSize = VesselSizeEnum.LESS_THAN_12m,
                identityControlledPerson = "John Doe"
            )
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertFalse(result.errors.any { it.field == "controlMethod" })
            assertFalse(result.errors.any { it.field == "latitude" })
        }

        @Test
        fun `should require numberPersonsRescued when isPersonRescue is true`() {
            val entity = createNavAction(
                actionType = ActionType.RESCUE,
                isPersonRescue = true,
                numberPersonsRescued = null,
                latitude = 48.0,
                longitude = 2.0
            )
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertTrue(result.errors.any { it.field == "numberPersonsRescued" })
        }

        @Test
        fun `should require reason when STATUS with DOCKED status`() {
            val entity = createNavAction(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                reason = null
            )
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            assertTrue(result.errors.any { it.field == "reason" })
        }

        @Test
        fun `should not check fields with ValidateThrowsBeforeSave group`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                controlMethod = null
            )
            val result = EntityValidityValidator.validateStatic(entity, ValidateThrowsBeforeSave::class.java)

            assertFalse(result.errors.any { it.field == "controlMethod" })
        }

        @Test
        fun `should require endDateTimeUtc for ILLEGAL_IMMIGRATION`() {
            val entity = createNavAction(
                actionType = ActionType.ILLEGAL_IMMIGRATION,
                endDateTimeUtc = null,
                latitude = 48.0,
                longitude = 2.0
            )
            val result = EntityValidityValidator.validateStatic(entity, ValidateWhenMissionFinished::class.java)

            println("Entity class: ${entity::class.java}")
            println("Validation result: ${result.status}")
            println("Errors: ${result.errors}")

            assertFalse(result.isComplete, "Should be invalid when endDateTimeUtc is null for ILLEGAL_IMMIGRATION")
            assertTrue(result.errors.any { it.field == "endDateTimeUtc" }, "Should have endDateTimeUtc error")
        }
    }
}
