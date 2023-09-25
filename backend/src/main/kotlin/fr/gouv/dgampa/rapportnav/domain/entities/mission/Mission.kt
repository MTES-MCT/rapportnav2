package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.controlResources.ControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.FishMission
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class Mission(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<ControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val closedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val isClosed: Boolean,
    val isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    val actions: List<MissionAction>?,
) {
    constructor(envMission: EnvMission, fishMission: FishMission? = null) : this(
        id = envMission.id ?: fishMission?.mission?.id,
        missionTypes = envMission.missionTypes,
        controlUnits = envMission.controlUnits,
        openBy = envMission.openBy,
        closedBy = envMission.closedBy,
        observationsCacem = envMission.observationsCacem,
        observationsCnsp = envMission.observationsCnsp,
        facade = envMission.facade,
        geom = envMission.geom,
        startDateTimeUtc = envMission.startDateTimeUtc,
        endDateTimeUtc = envMission.endDateTimeUtc,
        isClosed = envMission.isClosed,
        isDeleted = envMission.isDeleted,
        isGeometryComputedFromControls = envMission.isGeometryComputedFromControls,
        missionSource = envMission.missionSource,
        hasMissionOrder = envMission.hasMissionOrder,
        isUnderJdp = envMission.isUnderJdp,
        actions = (envMission.envActions?.map { MissionAction.EnvAction(it) } ?: listOf()) +
                (fishMission?.actions?.map { MissionAction.FishAction(it) } ?: listOf())
    )
}
