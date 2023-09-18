package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.FishMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.MissionSource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.MissionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.fishActions.MissionActionType
import java.time.ZonedDateTime


@UseCase
class GetFishMissionById {
    fun execute(missionId: Int): FishMission {
        // 1. MonitorFish - Get main Mission and MissionActions
        // TODO replace with API call

        val missionAction1 = MissionAction(
            missionId = 20,
            faoAreas = listOf("something"),
            actionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = ZonedDateTime.parse("2022-02-15T18:50:09Z"),
            flightGoals = listOf(),
            logbookInfractions = listOf(),
            gearInfractions = listOf(),
            speciesInfractions = listOf(),
            otherInfractions = listOf(),
            segments = listOf(),
            gearOnboard = listOf(),
            speciesOnboard = listOf(),
            controlUnits = listOf(),
            isDeleted = false,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
        )

        val mission = Mission(
            id = 20,
            missionTypes = listOf(MissionType.SEA),
            facade = "Outre-Mer",
            observationsCacem = null,
            startDateTimeUtc = ZonedDateTime.parse("2022-02-15T14:50:09Z"),
            endDateTimeUtc = ZonedDateTime.parse("2022-02-23T20:29:03Z"),
            isClosed = false,
            missionSource = MissionSource.MONITORFISH,
            hasMissionOrder = false,
            isUnderJdp = false,
        )

        val fishMission = FishMission(
            mission=mission,
            actions = listOf(missionAction1)
        )

        return fishMission
    }
}
