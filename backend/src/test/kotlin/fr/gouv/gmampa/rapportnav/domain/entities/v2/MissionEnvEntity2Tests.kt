package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.LegacyControlUnitEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class MissionEnvEntity2Tests {

    @Test
    fun `calculateMissionStatus should return UNAVAILABLE if endDateTime is null`() {
        val mission = MissionEntity()
        val result = mission.calculateMissionStatus(startDateTimeUtc = Instant.now(), endDateTimeUtc = null)
        assertEquals(MissionStatusEnum.UNAVAILABLE, result)
    }

    @Test
    fun `calculateMissionStatus should return IN_PROGRESS when endDateTime is after now`() {
        val mission = MissionEntity()
        val result = mission.calculateMissionStatus(
            startDateTimeUtc = Instant.now().minusSeconds(3600),
            endDateTimeUtc = Instant.now().plusSeconds(3600)
        )
        assertEquals(MissionStatusEnum.IN_PROGRESS, result)
    }

    @Test
    fun `calculateMissionStatus should return ENDED when endDateTime is before now`() {
        val mission = MissionEntity()
        val result = mission.calculateMissionStatus(
            startDateTimeUtc = Instant.now().minusSeconds(7200),
            endDateTimeUtc = Instant.now().minusSeconds(3600)
        )
        assertEquals(MissionStatusEnum.ENDED, result)
    }

    @Test
    fun `calculateMissionStatus should return UPCOMING when startDateTime is after now`() {
        val mission = MissionEntity()
        val future = Instant.now().plusSeconds(3600)
        val result = mission.calculateMissionStatus(
            startDateTimeUtc = future,
            endDateTimeUtc = future.plusSeconds(3600)
        )
        assertEquals(MissionStatusEnum.UPCOMING, result)
    }

    @Test
    fun `isCompleteForStats should return COMPLETE when general info is complete and actions are complete`() {
        val action = MissionEnvActionEntityMock.create(completion = ActionCompletionEnum.COMPLETED)
        action.isCompleteForStats = true

        val generalInfo = MissionGeneralInfoEntity2Mock.create(
            data = MissionGeneralInfoEntityMock.create(
                distanceInNauticalMiles = 1F,
                consumedGOInLiters = 1F,
                consumedFuelInLiters = 1F,
                nbrOfRecognizedVessel = 1,
                service = ServiceEntityMock.create(id = 1),
            ),
            crew = listOf(MissionCrewEntityMock.create())
        )
        val mission = MissionEntity(
            actions = listOf(action),
            generalInfos = generalInfo,
            data = EnvMissionMock.create(observationsByUnit = "bla")
        )

        val result: CompletenessForStatsEntity = mission.isCompleteForStats()

        assertEquals(CompletenessForStatsStatusEnum.COMPLETE, result.status)
        assertTrue(result.sources!!.isEmpty())
    }

    @Test
    fun `isCompleteForStats should return INCOMPLETE when general info is incomplete`() {
        val action = MissionEnvActionEntityMock.create(completion = ActionCompletionEnum.COMPLETED)
        action.isCompleteForStats = true

        val generalInfo = MissionGeneralInfoEntity2Mock.create(
            data = MissionGeneralInfoEntity(
                distanceInNauticalMiles = null, // it shouldn't be null
                consumedGOInLiters = 1F,
                consumedFuelInLiters = 1F,
                nbrOfRecognizedVessel = 1,
            ),
            crew = listOf(MissionCrewEntityMock.create())
        )
        val mission = MissionEntity(
            actions = listOf(action),
            generalInfos = generalInfo,
            data = EnvMissionMock.create(observationsByUnit = "bla")
        )

        val result: CompletenessForStatsEntity = mission.isCompleteForStats()

        assertEquals(CompletenessForStatsStatusEnum.INCOMPLETE, result.status)
        assertTrue(result.sources!!.contains(MissionSourceEnum.RAPPORTNAV))
    }

    @Test
    fun `isCompleteForStats should return COMPLETE when observationsByUnit is null`() {
        val generalInfo = MissionGeneralInfoEntity2Mock.create()
        val mission = MissionEntity(
            actions = listOf(),
            generalInfos = generalInfo,
            data = EnvMissionMock.create(observationsByUnit = null)
        )

        val result: CompletenessForStatsEntity = mission.isCompleteForStats()

        assertEquals(CompletenessForStatsStatusEnum.COMPLETE, result.status)
        assertTrue(!result.sources!!.contains(MissionSourceEnum.RAPPORTNAV))
    }

    @Test
    fun `isCompleteForStats should return COMPLETE when actions are complete for secondary missions`() {
        val action = MissionEnvActionEntityMock.create(completion = ActionCompletionEnum.COMPLETED)
        action.isCompleteForStats = true

        val generalInfo = MissionGeneralInfoEntity2Mock.create(
            data = MissionGeneralInfoEntity(
                distanceInNauticalMiles = null, // it is null but not a problem for secondary missions
                consumedGOInLiters = 1F,
                consumedFuelInLiters = 1F,
                nbrOfRecognizedVessel = 1,
            ),
            crew = listOf(MissionCrewEntityMock.create())
        )
        val mission = MissionEntity(
            actions = listOf(action),
            generalInfos = generalInfo,
            data = EnvMissionMock.create(
                observationsByUnit = null, // it is null but not a problem for secondary missions
                controlUnits = listOf(
                    LegacyControlUnitEntityMock.create(id = 1, administration = "ABC"),
                    LegacyControlUnitEntityMock.create(id = 2, administration = "XYZ"),
                    LegacyControlUnitEntityMock.create(id = 3, administration = "XYZ"),
                )
            ),
        )

        val result: CompletenessForStatsEntity = mission.isCompleteForStats()

        assertEquals(CompletenessForStatsStatusEnum.COMPLETE, result.status)
        assertTrue(result.sources!!.isEmpty())
    }

}
