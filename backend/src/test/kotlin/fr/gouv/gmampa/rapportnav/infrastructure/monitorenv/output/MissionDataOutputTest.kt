package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime

class MissionDataOutputTest {

    @Nested
    inner class ToMissionEntity {

        @Test
        fun `should convert to MissionEntity with all required fields`() {
            val startDateTime = ZonedDateTime.of(2024, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC)
            val endDateTime = ZonedDateTime.of(2024, 1, 15, 18, 0, 0, 0, ZoneOffset.UTC)

            val output = MissionDataOutput(
                id = 123,
                missionTypes = listOf(MissionTypeEnum.SEA),
                controlUnits = listOf(),
                startDateTimeUtc = startDateTime,
                endDateTimeUtc = endDateTime,
                missionSource = MissionSourceEnum.MONITORENV,
                hasMissionOrder = true,
                isUnderJdp = false,
                isGeometryComputedFromControls = true,
                observationsCacem = "CACEM observations",
                observationsCnsp = "CNSP observations",
                observationsByUnit = "Unit observations"
            )

            val result = output.toMissionEnvEntity()

            assertEquals(123, result.id)
            assertEquals(listOf(MissionTypeEnum.SEA), result.missionTypes)
            assertEquals(startDateTime.toInstant(), result.startDateTimeUtc)
            assertEquals(endDateTime.toInstant(), result.endDateTimeUtc)
            assertEquals(MissionSourceEnum.MONITORENV, result.missionSource)
            assertTrue(result.hasMissionOrder!!)
            assertFalse(result.isUnderJdp!!)
            assertTrue(result.isGeometryComputedFromControls!!)
            assertFalse(result.isDeleted!!)
            assertEquals("CACEM observations", result.observationsCacem)
            assertEquals("CNSP observations", result.observationsCnsp)
            assertEquals("Unit observations", result.observationsByUnit)
        }

        @Test
        fun `should convert ZonedDateTime to Instant correctly`() {
            val startDateTime = ZonedDateTime.parse("2024-06-15T14:30:00+02:00")
            val endDateTime = ZonedDateTime.parse("2024-06-15T20:45:00+02:00")

            val output = MissionDataOutput(
                id = 1,
                missionTypes = listOf(MissionTypeEnum.LAND),
                startDateTimeUtc = startDateTime,
                endDateTimeUtc = endDateTime,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,
                isUnderJdp = false,
                isGeometryComputedFromControls = false
            )

            val result = output.toMissionEnvEntity()

            assertEquals(startDateTime.toInstant(), result.startDateTimeUtc)
            assertEquals(endDateTime.toInstant(), result.endDateTimeUtc)
        }

        @Test
        fun `should handle null optional fields`() {
            val output = MissionDataOutput(
                id = 1,
                missionTypes = listOf(MissionTypeEnum.SEA),
                controlUnits = null,
                openBy = null,
                completedBy = null,
                observationsCacem = null,
                observationsCnsp = null,
                facade = null,
                geom = null,
                startDateTimeUtc = ZonedDateTime.now(),
                endDateTimeUtc = null,
                envActions = null,
                missionSource = MissionSourceEnum.MONITORENV,
                hasMissionOrder = false,
                isUnderJdp = false,
                isGeometryComputedFromControls = false,
                observationsByUnit = null
            )

            val result = output.toMissionEnvEntity()

            assertTrue(result.controlUnits?.isEmpty() ?: true)
            assertNull(result.openBy)
            assertNull(result.completedBy)
            assertNull(result.observationsCacem)
            assertNull(result.observationsCnsp)
            assertNull(result.facade)
            assertNull(result.geom)
            assertNull(result.endDateTimeUtc)
            assertNull(result.envActions)
            assertNull(result.observationsByUnit)
        }

        @Test
        fun `should map control units correctly`() {
            val controlUnit = LegacyControlUnitEntity(
                id = 10,
                name = "PAM Themis",
                administration = "DIRM NAMO",
                isArchived = false,
                resources = mutableListOf()
            )

            val output = MissionDataOutput(
                id = 1,
                missionTypes = listOf(MissionTypeEnum.SEA),
                controlUnits = listOf(controlUnit),
                startDateTimeUtc = ZonedDateTime.now(),
                missionSource = MissionSourceEnum.MONITORENV,
                hasMissionOrder = false,
                isUnderJdp = false,
                isGeometryComputedFromControls = false
            )

            val result = output.toMissionEnvEntity()

            assertEquals(1, result.controlUnits.size)
            assertEquals(10, result.controlUnits[0].id)
            assertEquals("PAM Themis", result.controlUnits[0].name)
        }

    }

    @Nested
    inner class ToMissionEnvEntity {

        @Test
        fun `should convert to MissionEnvEntity with all required fields`() {
            val startDateTime = ZonedDateTime.of(2024, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC)

            val output = MissionDataOutput(
                id = 123,
                missionTypes = listOf(MissionTypeEnum.SEA),
                controlUnits = listOf(),
                startDateTimeUtc = startDateTime,
                missionSource = MissionSourceEnum.MONITORENV,
                hasMissionOrder = true,
                isUnderJdp = true,
                isGeometryComputedFromControls = false
            )

            val result = output.toMissionEnvEntity()

            assertEquals(123, result.id)
            assertEquals(listOf(MissionTypeEnum.SEA), result.missionTypes)
            assertEquals(startDateTime.toInstant(), result.startDateTimeUtc)
            assertEquals(MissionSourceEnum.MONITORENV, result.missionSource)
            assertEquals(true, result.hasMissionOrder)
            assertEquals(true, result.isUnderJdp)
            assertEquals(false, result.isGeometryComputedFromControls)
        }

        @Test
        fun `should convert null controlUnits to empty list`() {
            val output = MissionDataOutput(
                id = 1,
                missionTypes = listOf(MissionTypeEnum.SEA),
                controlUnits = null,
                startDateTimeUtc = ZonedDateTime.now(),
                missionSource = MissionSourceEnum.MONITORENV,
                hasMissionOrder = false,
                isUnderJdp = false,
                isGeometryComputedFromControls = false
            )

            val result = output.toMissionEnvEntity()

            assertTrue(result.controlUnits?.isEmpty() ?: true)
        }
    }
}
