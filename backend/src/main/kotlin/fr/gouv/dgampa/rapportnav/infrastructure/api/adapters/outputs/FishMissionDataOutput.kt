package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.*
import java.time.ZonedDateTime

data class FishMissionDataOutput(
    val id: Int,
    val missionTypes: List<MissionType>,
    val openBy: String? = null,
    val closedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val missionSource: MissionSource,
    val isClosed: Boolean,
    val hasMissionOrder: Boolean? = false,
    val isUnderJdp: Boolean? = false,
    val controlUnits: List<ControlUnit> = listOf(),
    val actions: List<FishMissionActionDataOutput>,
) {
    companion object {
        fun fromMissionAndActions(missionAndActions: MissionAndActions) = FishMissionDataOutput(
            id = missionAndActions.mission.id,
            missionTypes = missionAndActions.mission.missionTypes,
            openBy = missionAndActions.mission.openBy,
            closedBy = missionAndActions.mission.closedBy,
            observationsCacem = missionAndActions.mission.observationsCacem,
            observationsCnsp = missionAndActions.mission.observationsCnsp,
            facade = missionAndActions.mission.facade,
            geom = missionAndActions.mission.geom,
            startDateTimeUtc = missionAndActions.mission.startDateTimeUtc,
            endDateTimeUtc = missionAndActions.mission.endDateTimeUtc,
            missionSource = missionAndActions.mission.missionSource,
            isClosed = missionAndActions.mission.isClosed,
            hasMissionOrder = missionAndActions.mission.hasMissionOrder,
            isUnderJdp = missionAndActions.mission.isUnderJdp,
            controlUnits = missionAndActions.mission.controlUnits,
            actions = missionAndActions.actions.map { FishMissionActionDataOutput.fromMissionAction(it) },
        )
    }
}
