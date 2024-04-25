package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

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
        startDateTimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        endDateTimeUtc: ZonedDateTime? = null,
        isClosed: Boolean = false,
        missionSource: MissionSource = MissionSource.MONITORENV,
        hasMissionOrder: Boolean = false,
        isUnderJdp: Boolean = false,
        actions: List<MissionAction> = listOf(),
    ): MissionAndActions {
        return MissionAndActions(
            mission = Mission(
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
                isClosed = isClosed,
                missionSource = missionSource,
                hasMissionOrder = hasMissionOrder,
                isUnderJdp = isUnderJdp,
            ),
            actions = actions
        )
    }

}
