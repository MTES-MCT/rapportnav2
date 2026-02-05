package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import java.time.Instant

object FishMissionMock {
    fun create(
        id: Int = 1,
        missionTypes: List<MissionType> = listOf(),
        controlUnits: List<ControlUnit> = listOf(),
        openBy: String? = null,
        observationsCacem: String? = null,
        observationsCnsp: String? = null,
        facade: String? = null,
        geom: MultiPolygon? = null,
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:01Z"),
        endDateTimeUtc: Instant? = null,
        missionSource: MissionSource = MissionSource.MONITORENV,
        hasMissionOrder: Boolean = false,
        isUnderJdp: Boolean = false,
        actions: List<MissionAction> = listOf(),
    ): MissionAndActions {
        return MissionAndActions(
            mission = MissionFishEntity(
                id = id,
                missionTypes = missionTypes,
                controlUnits = controlUnits,
                openBy = openBy,
                observationsCacem = observationsCacem,
                observationsCnsp = observationsCnsp,
                facade = facade,
                geom = geom,
                startDateTimeUtc = startDateTimeUtc,
                endDateTimeUtc = endDateTimeUtc,
                missionSource = missionSource,
                hasMissionOrder = hasMissionOrder,
                isUnderJdp = isUnderJdp,
            ),
            actions = actions
        )
    }

}
