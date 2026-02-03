package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish.output

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.MissionActionDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.MissionActionInfractionDataOutput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime

class MissionActionDataOutputTest {

    @Nested
    inner class ToMissionAction {

        @Test
        fun `should convert to MissionAction with all required fields`() {
            val actionDatetime = ZonedDateTime.of(2024, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC)

            val output = MissionActionDataOutput(
                id = 123,
                missionId = 456,
                flagState = CountryCode.FR,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = actionDatetime,
                userTrigram = "JDO",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = true
            )

            val result = output.toMissionAction()

            assertEquals(123, result.id)
            assertEquals(456, result.missionId)
            assertEquals(CountryCode.FR, result.flagState)
            assertEquals(MissionActionType.SEA_CONTROL, result.actionType)
            assertEquals(actionDatetime.toInstant(), result.actionDatetimeUtc)
            assertEquals("JDO", result.userTrigram)
            assertEquals(Completion.COMPLETED, result.completion)
            assertFalse(result.hasSomeGearsSeized)
            assertFalse(result.hasSomeSpeciesSeized)
            assertFalse(result.isDeleted)
            assertTrue(result.isFromPoseidon)
        }

        @Test
        fun `should convert ZonedDateTime to Instant correctly`() {
            val actionDatetime = ZonedDateTime.parse("2024-06-15T14:30:00+02:00")
            val endDatetime = ZonedDateTime.parse("2024-06-15T16:45:00+02:00")

            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.LAND_CONTROL,
                actionDatetimeUtc = actionDatetime,
                actionEndDatetimeUtc = endDatetime,
                userTrigram = "ABC",
                completion = Completion.TO_COMPLETE,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false
            )

            val result = output.toMissionAction()

            assertEquals(actionDatetime.toInstant(), result.actionDatetimeUtc)
            assertEquals(endDatetime.toInstant(), result.actionEndDatetimeUtc)
        }

        @Test
        fun `should handle null optional fields`() {
            val output = MissionActionDataOutput(
                id = null,
                missionId = 100,
                vesselId = null,
                vesselName = null,
                internalReferenceNumber = null,
                externalReferenceNumber = null,
                ircs = null,
                flagState = CountryCode.FR,
                districtCode = null,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                actionEndDatetimeUtc = null,
                userTrigram = "XYZ",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false
            )

            val result = output.toMissionAction()

            assertNull(result.id)
            assertNull(result.vesselId)
            assertNull(result.vesselName)
            assertNull(result.internalReferenceNumber)
            assertNull(result.externalReferenceNumber)
            assertNull(result.ircs)
            assertNull(result.districtCode)
            assertNull(result.actionEndDatetimeUtc)
        }

        @Test
        fun `should map vessel identification fields`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                vesselId = 789,
                vesselName = "Le Pecheur",
                internalReferenceNumber = "FRA000123456",
                externalReferenceNumber = "BR123456",
                ircs = "FABCD",
                flagState = CountryCode.FR,
                districtCode = "BR",
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false
            )

            val result = output.toMissionAction()

            assertEquals(789, result.vesselId)
            assertEquals("Le Pecheur", result.vesselName)
            assertEquals("FRA000123456", result.internalReferenceNumber)
            assertEquals("BR123456", result.externalReferenceNumber)
            assertEquals("FABCD", result.ircs)
            assertEquals("BR", result.districtCode)
        }

        @Test
        fun `should map control check fields`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false,
                emitsVms = ControlCheck.YES,
                emitsAis = ControlCheck.NO,
                logbookMatchesActivity = ControlCheck.NOT_APPLICABLE,
                licencesMatchActivity = ControlCheck.YES,
                separateStowageOfPreservedSpecies = ControlCheck.NO,
                vesselTargeted = ControlCheck.YES
            )

            val result = output.toMissionAction()

            assertEquals(ControlCheck.YES, result.emitsVms)
            assertEquals(ControlCheck.NO, result.emitsAis)
            assertEquals(ControlCheck.NOT_APPLICABLE, result.logbookMatchesActivity)
            assertEquals(ControlCheck.YES, result.licencesMatchActivity)
            assertEquals(ControlCheck.NO, result.separateStowageOfPreservedSpecies)
            assertEquals(ControlCheck.YES, result.vesselTargeted)
        }

        @Test
        fun `should map location fields`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.LAND_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false,
                longitude = -4.48,
                latitude = 48.39,
                portLocode = "FRBST",
                portName = "Brest",
                facade = "NAMO"
            )

            val result = output.toMissionAction()

            assertEquals(-4.48, result.longitude)
            assertEquals(48.39, result.latitude)
            assertEquals("FRBST", result.portLocode)
            assertEquals("Brest", result.portName)
            assertEquals("NAMO", result.facade)
        }

        @Test
        fun `should map boolean control flags`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = true,
                hasSomeSpeciesSeized = true,
                isDeleted = false,
                isFromPoseidon = false,
                speciesWeightControlled = true,
                speciesSizeControlled = false,
                seizureAndDiversion = true,
                unitWithoutOmegaGauge = false,
                isAdministrativeControl = true,
                isComplianceWithWaterRegulationsControl = false,
                isSafetyEquipmentAndStandardsComplianceControl = true,
                isSeafarersControl = false
            )

            val result = output.toMissionAction()

            assertTrue(result.hasSomeGearsSeized)
            assertTrue(result.hasSomeSpeciesSeized)
            assertEquals(true, result.speciesWeightControlled)
            assertEquals(false, result.speciesSizeControlled)
            assertEquals(true, result.seizureAndDiversion)
            assertEquals(false, result.unitWithoutOmegaGauge)
            assertEquals(true, result.isAdministrativeControl)
            assertEquals(false, result.isComplianceWithWaterRegulationsControl)
            assertEquals(true, result.isSafetyEquipmentAndStandardsComplianceControl)
            assertEquals(false, result.isSeafarersControl)
        }

        @Test
        fun `should map comment fields`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false,
                licencesAndLogbookObservations = "Licences observations",
                speciesObservations = "Species observations",
                controlQualityComments = "Quality comments",
                seizureAndDiversionComments = "Seizure comments",
                otherComments = "Other comments",
                observationsByUnit = "Unit observations"
            )

            val result = output.toMissionAction()

            assertEquals("Licences observations", result.licencesAndLogbookObservations)
            assertEquals("Species observations", result.speciesObservations)
            assertEquals("Quality comments", result.controlQualityComments)
            assertEquals("Seizure comments", result.seizureAndDiversionComments)
            assertEquals("Other comments", result.otherComments)
            assertEquals("Unit observations", result.observationsByUnit)
        }

        @Test
        fun `should map list fields with default empty lists`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false
            )

            val result = output.toMissionAction()

            assertTrue(result.faoAreas.isEmpty())
            assertTrue(result.flightGoals.isEmpty())
            assertTrue(result.segments.isEmpty())
            assertTrue(result.gearOnboard.isEmpty())
            assertTrue(result.speciesOnboard.isEmpty())
            assertTrue(result.controlUnits.isEmpty())
        }

        @Test
        fun `should map fao areas list`() {
            val output = MissionActionDataOutput(
                id = 1,
                missionId = 100,
                flagState = CountryCode.FR,
                actionType = MissionActionType.SEA_CONTROL,
                actionDatetimeUtc = ZonedDateTime.now(),
                userTrigram = "ABC",
                completion = Completion.COMPLETED,
                hasSomeGearsSeized = false,
                hasSomeSpeciesSeized = false,
                isDeleted = false,
                isFromPoseidon = false,
                faoAreas = listOf("27.7.e", "27.8.a")
            )

            val result = output.toMissionAction()

            assertEquals(2, result.faoAreas.size)
            assertTrue(result.faoAreas.contains("27.7.e"))
            assertTrue(result.faoAreas.contains("27.8.a"))
        }

        @Test
        fun `should map different action types`() {
            val actionTypes = listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL,
                MissionActionType.AIR_CONTROL,
                MissionActionType.AIR_SURVEILLANCE,
                MissionActionType.OBSERVATION
            )

            actionTypes.forEach { actionType ->
                val output = MissionActionDataOutput(
                    id = 1,
                    missionId = 100,
                    flagState = CountryCode.FR,
                    actionType = actionType,
                    actionDatetimeUtc = ZonedDateTime.now(),
                    userTrigram = "ABC",
                    completion = Completion.COMPLETED,
                    hasSomeGearsSeized = false,
                    hasSomeSpeciesSeized = false,
                    isDeleted = false,
                    isFromPoseidon = false
                )

                val result = output.toMissionAction()

                assertEquals(actionType, result.actionType)
            }
        }
    }

    @Nested
    inner class MissionActionInfractionDataOutputFromInfraction {

        @Test
        fun `should create from infraction with all fields`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 27718,
                natinfDescription = "Illegal landing",
                threat = "Mesures techniques",
                threatCharacterization = "Autorisation",
                comments = "Some comments"
            )

            val result = MissionActionInfractionDataOutput.fromInfraction(infraction)

            assertEquals(InfractionType.WITH_RECORD, result.infractionType)
            assertEquals(27718, result.natinf)
            assertEquals("Illegal landing", result.natinfDescription)
            assertEquals("Mesures techniques", result.threat)
            assertEquals("Autorisation", result.threatCharacterization)
            assertEquals("Some comments", result.comments)
        }

        @Test
        fun `should use fallback values for null threat and characterization`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITHOUT_RECORD,
                natinf = 12345,
                threat = null,
                threatCharacterization = null
            )

            val result = MissionActionInfractionDataOutput.fromInfraction(infraction)

            assertEquals("Famille inconnue", result.threat)
            assertEquals("Type inconnu", result.threatCharacterization)
        }

        @Test
        fun `should create from infraction with threat hierarchy`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 27718,
                threat = "Conservation",
                threatCharacterization = "Fishing Gear"
            )

            val result = MissionActionInfractionDataOutput.fromInfractionWithThreatHierarchy(infraction)

            assertEquals(InfractionType.WITH_RECORD, result.infractionType)
            assertNotNull(result.threats)
            assertEquals(1, result.threats!!.size)
        }
    }
}