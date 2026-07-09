package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EstablishmentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.LocationType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class RequiredFieldsValidatorTest {

    private val validator = EntityValidityValidator.createDefault()

    // =========================================================================
    // MissionGeneralInfoEntity tests
    // =========================================================================
    @Nested
    @DisplayName("MissionGeneralInfoEntity")
    inner class GeneralInfoTests {

        private val pamService = ServiceEntity(name = "PAM Test", serviceType = ServiceTypeEnum.PAM)
        private val ulamService = ServiceEntity(name = "ULAM Test", serviceType = ServiceTypeEnum.ULAM)

        @Test
        fun `PAM - should be valid when all required fields are present`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                service = pamService,
                distanceInNauticalMiles = 50.5f,
                consumedGOInLiters = 100.0f,
                consumedFuelInLiters = 200.0f,
                nbrOfRecognizedVessel = 5
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.isComplete)
        }

        @Test
        fun `PAM - should be invalid when required fields are null`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                service = pamService,
                distanceInNauticalMiles = null,
                consumedGOInLiters = null,
                consumedFuelInLiters = null,
                nbrOfRecognizedVessel = null
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.isComplete)
            assertEquals(4, result.errors.size)
        }

        @Test
        fun `ULAM - should not require PAM fields`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                service = ulamService,
                distanceInNauticalMiles = null,
                consumedGOInLiters = null,
                consumedFuelInLiters = null,
                nbrOfRecognizedVessel = null
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.isComplete)
        }

        @Test
        fun `ULAM - should require interMinisterialServices when isWithInterMinisterialService is true`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                service = ulamService,
                isWithInterMinisterialService = true,
                interMinisterialServices = listOf()
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.isComplete)
            assertTrue(result.errors.any { it.field == "interMinisterialServices" })
        }

        @Test
        fun `ULAM - should be valid when isWithInterMinisterialService is true and services provided`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                service = ulamService,
                isWithInterMinisterialService = true,
                interMinisterialServices = listOf(
                    InterMinisterialServiceEntity(administrationId = 1, controlUnitId = 1)
                )
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.errors.any { it.field == "interMinisterialServices" })
        }

        @Test
        fun `ULAM - should not require interMinisterialServices when isWithInterMinisterialService is false`() {
            val entity = MissionGeneralInfoEntity(
                id = 1,
                missionId = 100,
                service = ulamService,
                isWithInterMinisterialService = false
            )

            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.isComplete)
        }

        @Test
        fun `should not validate with ValidateThrowsBeforeSave group`() {
            val entity = MissionGeneralInfoEntity(
                service = pamService,
                distanceInNauticalMiles = null
            )

            val result = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

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
            locationType: LocationType? = null,
            vesselIdentifier: String? = null,
            vesselType: VesselTypeEnum? = null,
            vesselSize: VesselSizeEnum? = null,
            identityControlledPerson: String? = null,
            isPersonRescue: Boolean? = null,
            numberPersonsRescued: Int? = null,
            status: ActionStatusType? = null,
            reason: ActionStatusReason? = null,
            isMigrationRescue: Boolean? = null,
            city: String? = null,
            zipCode: String? = null,
            portLocode: String? = null,
            sectorType: SectorType? = null,
            sectorEstablishmentType: SectorEstablishmentType? = null,
            fishAuction: FishAuctionEntity? = null,
            establishment: EstablishmentEntity? = null
        ) = MissionNavActionEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            actionType = actionType,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            latitude = latitude,
            longitude = longitude,
            controlMethod = controlMethod,
            locationType = locationType,
            vesselIdentifier = vesselIdentifier,
            vesselType = vesselType,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            isPersonRescue = isPersonRescue,
            numberPersonsRescued = numberPersonsRescued,
            status = status,
            reason = reason,
            isMigrationRescue = isMigrationRescue,
            city = city,
            zipCode = zipCode,
            portLocode = portLocode,
            sectorType = sectorType,
            sectorEstablishmentType = sectorEstablishmentType,
            fishAuction = fishAuction,
            establishment = establishment
        )

        @Test
        fun `should be valid when all always-required fields are present`() {
            val entity = createNavAction(actionType = ActionType.OTHER)
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.isComplete)
        }

        @Test
        fun `should be invalid when startDateTimeUtc is null`() {
            val entity = createNavAction(startDateTimeUtc = null)
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.isComplete)
            assertTrue(result.errors.any { it.field == "startDateTimeUtc" })
        }

        @Test
        fun `should require CONTROL-specific fields`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                controlMethod = null,
                locationType = LocationType.GPS,
                latitude = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.isComplete)
            assertTrue(result.errors.any { it.field == "controlMethod" })
            assertTrue(result.errors.any { it.field == "latitude" })
        }

        @Test
        fun `should be valid when CONTROL has all required fields`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                locationType = LocationType.GPS,
                latitude = 48.8566,
                longitude = 2.3522,
                controlMethod = ControlMethod.SEA,
                vesselIdentifier = "ABC123",
                vesselType = VesselTypeEnum.FISHING,
                vesselSize = VesselSizeEnum.LESS_THAN_12m,
                identityControlledPerson = "John Doe"
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

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
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "numberPersonsRescued" })
        }

        @Test
        fun `should require reason when STATUS with DOCKED status`() {
            val entity = createNavAction(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                reason = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "reason" })
        }

        @Test
        fun `should not check fields with ValidateThrowsBeforeSave group`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                controlMethod = null
            )
            val result = validator.validate(entity, ValidateThrowsBeforeSave::class.java)

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
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.isComplete, "Should be invalid when endDateTimeUtc is null for ILLEGAL_IMMIGRATION")
            assertTrue(result.errors.any { it.field == "endDateTimeUtc" }, "Should have endDateTimeUtc error")
        }

        // =====================================================================
        // Feature 1: Location-type-dependent validation
        // =====================================================================

        @Test
        fun `should require latitude and longitude when locationType is GPS for CONTROL`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                locationType = LocationType.GPS,
                latitude = null,
                longitude = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "latitude" })
            assertTrue(result.errors.any { it.field == "longitude" })
        }

        @Test
        fun `should require city and zipCode when locationType is COMMUNE for CONTROL`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                locationType = LocationType.COMMUNE,
                city = null,
                zipCode = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "city" })
            assertTrue(result.errors.any { it.field == "zipCode" })
        }

        @Test
        fun `should require portLocode when locationType is PORT for CONTROL`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                locationType = LocationType.PORT,
                portLocode = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "portLocode" })
        }

        @Test
        fun `should not require latitude when locationType is COMMUNE for CONTROL`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL,
                locationType = LocationType.COMMUNE,
                city = "Paris",
                zipCode = "75001",
                latitude = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.errors.any { it.field == "latitude" })
        }

        @Test
        fun `should require latitude and longitude when locationType is GPS for CONTROL_NAUTICAL_LEISURE`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL_NAUTICAL_LEISURE,
                locationType = LocationType.GPS,
                latitude = null,
                longitude = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "latitude" })
            assertTrue(result.errors.any { it.field == "longitude" })
        }

        @Test
        fun `should require city and zipCode when locationType is COMMUNE for OTHER_CONTROL`() {
            val entity = createNavAction(
                actionType = ActionType.OTHER_CONTROL,
                locationType = LocationType.COMMUNE,
                city = null,
                zipCode = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "city" })
            assertTrue(result.errors.any { it.field == "zipCode" })
        }

        @Test
        fun `should require portLocode when locationType is PORT for CONTROL_SLEEPING_FISHING_GEAR`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL_SLEEPING_FISHING_GEAR,
                locationType = LocationType.PORT,
                portLocode = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "portLocode" })
        }

        // =====================================================================
        // Feature 2: CONTROL_SECTOR + FISHING sector type
        // =====================================================================

        @Test
        fun `should require fishAuction when CONTROL_SECTOR with FISHING and FISH_AUCTION`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL_SECTOR,
                sectorType = SectorType.FISHING,
                sectorEstablishmentType = SectorEstablishmentType.FISH_AUCTION,
                fishAuction = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "fishAuction" })
        }

        @Test
        fun `should require portLocode when CONTROL_SECTOR with FISHING and LANDING_SITE`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL_SECTOR,
                sectorType = SectorType.FISHING,
                sectorEstablishmentType = SectorEstablishmentType.LANDING_SITE,
                portLocode = null
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertTrue(result.errors.any { it.field == "portLocode" })
        }

        @Test
        fun `should not require fishAuction when CONTROL_SECTOR with FISHING and LANDING_SITE`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL_SECTOR,
                sectorType = SectorType.FISHING,
                sectorEstablishmentType = SectorEstablishmentType.LANDING_SITE,
                portLocode = "FRBOD"
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.errors.any { it.field == "fishAuction" })
        }

        @Test
        fun `should not require fishAuction when CONTROL_SECTOR with PLEASURE sector`() {
            val entity = createNavAction(
                actionType = ActionType.CONTROL_SECTOR,
                sectorType = SectorType.PLEASURE,
                sectorEstablishmentType = SectorEstablishmentType.FISH_AUCTION
            )
            val result = validator.validateCompleteness(entity, ValidationPolicies.v1)

            assertFalse(result.errors.any { it.field == "fishAuction" })
        }
    }

    // =========================================================================
    // Policy resolution tests
    // =========================================================================
    @Nested
    @DisplayName("ValidationPolicies")
    inner class PolicyResolutionTests {

        @Test
        fun `should resolve v1 for missions before v2 effective date`() {
            val policy = ValidationPolicies.forMissionStartDate(Instant.parse("2026-01-15T00:00:00Z"))
            assertEquals(1, policy.version)
        }

        @Test
        fun `should resolve latest policy when start date is null`() {
            val policy = ValidationPolicies.forMissionStartDate(null)
            assertEquals(ValidationPolicies.latest.version, policy.version)
        }

        @Test
        fun `should resolve v1 for missions that predate all policies`() {
            val policy = ValidationPolicies.forMissionStartDate(Instant.parse("2024-01-01T00:00:00Z"))
            assertEquals(1, policy.version)
        }
    }


}
