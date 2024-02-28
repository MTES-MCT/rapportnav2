package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionEntity(
    val id: Int,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
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
    var actions: List<MissionActionEntity>? = null,
) {
    constructor(
        envMission: ExtendedEnvMissionEntity,
        navMission: NavMissionEntity? = null,
        fishMissionActions: List<ExtendedFishActionEntity>? = null
    ) : this(
        id = (envMission.mission.id)!!,
        missionTypes = envMission.mission.missionTypes,
        controlUnits = envMission.mission.controlUnits,
        openBy = envMission.mission.openBy,
        closedBy = envMission.mission.closedBy,
        observationsCacem = envMission.mission.observationsCacem,
        observationsCnsp = envMission.mission.observationsCnsp,
        facade = envMission.mission.facade,
        geom = envMission.mission.geom,
        startDateTimeUtc = envMission.mission.startDateTimeUtc,
        endDateTimeUtc = envMission.mission.endDateTimeUtc,
        isClosed = envMission.mission.isClosed,
        isDeleted = envMission.mission.isDeleted,
        isGeometryComputedFromControls = envMission.mission.isGeometryComputedFromControls,
        missionSource = envMission.mission.missionSource,
        hasMissionOrder = envMission.mission.hasMissionOrder,
        isUnderJdp = envMission.mission.isUnderJdp,
        actions = sortActions(
            envMission.actions?.map { MissionActionEntity.EnvAction(it) } ?: emptyList(),
            fishMissionActions?.map { MissionActionEntity.FishAction(it) } ?: emptyList(),
            navMission?.actions?.map { MissionActionEntity.NavAction(it) } ?: emptyList()
        )
    )

    companion object {
        private fun sortActions(
            envActions: List<MissionActionEntity.EnvAction>,
            fishActions: List<MissionActionEntity.FishAction>,
            navActions: List<MissionActionEntity.NavAction>
        ): List<MissionActionEntity> {
            return (envActions + fishActions + navActions).sortedBy { action ->
                when (action) {
                    is MissionActionEntity.EnvAction -> action.envAction?.controlAction?.action?.actionStartDateTimeUtc
                    is MissionActionEntity.FishAction -> action.fishAction.controlAction?.action?.actionDatetimeUtc
                    is MissionActionEntity.NavAction -> action.navAction.startDateTimeUtc
                }
            }
        }
    }
}

