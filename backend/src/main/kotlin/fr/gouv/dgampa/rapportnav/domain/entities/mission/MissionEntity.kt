package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionEntity(
    val id: Int,
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
    val actions: List<MissionActionEntity>?,
) {
    constructor(envMission: EnvMission, navMission: NavMissionEntity? = null, fishMission: FishMission? = null) : this(
        id = (envMission.id ?: fishMission?.mission?.id)!!,
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
        actions = (envMission.envActions?.map { MissionActionEntity.EnvAction(it) } ?: listOf()) +
                (fishMission?.actions?.map { MissionActionEntity.FishAction(it) } ?: listOf()) +
                (navMission?.actions?.map { MissionActionEntity.NavAction(it) } ?: listOf())

    )
}
