package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.controlResources.ControlUnitEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionDataOutput(
    val id: Int,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<ControlUnitEntity>? = listOf(),
    val openBy: String? = null,
    val closedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val missionSource: MissionSourceEnum,
    val isClosed: Boolean,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    val actions: List<MissionAction>? = null,
) {
    companion object {
        fun fromMission(mission: Mission): MissionDataOutput {
            requireNotNull(mission.id) {
                "a mission must have an id"
            }

            return MissionDataOutput(
                id = mission.id,
                missionTypes = mission.missionTypes,
                controlUnits = mission.controlUnits,
                openBy = mission.openBy,
                closedBy = mission.closedBy,
                observationsCacem = mission.observationsCacem,
                observationsCnsp = mission.observationsCnsp,
                facade = mission.facade,
                geom = mission.geom,
                startDateTimeUtc = mission.startDateTimeUtc,
                endDateTimeUtc = mission.endDateTimeUtc,
                missionSource = mission.missionSource,
                isClosed = mission.isClosed,
                hasMissionOrder = mission.hasMissionOrder,
                isUnderJdp = mission.isUnderJdp,
                actions = mission.actions
            )
        }
    }
}
