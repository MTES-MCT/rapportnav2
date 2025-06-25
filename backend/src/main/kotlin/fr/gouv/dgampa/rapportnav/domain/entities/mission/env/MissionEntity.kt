package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant
import java.util.*

//TODO: this have been rename to MissionEnvEntity for V2. Delete this after V2 migration
data class MissionEntity(
    val id: Int? = null,
    val idUUID: UUID? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf(),
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var envActions: List<EnvActionEntity>? = listOf(),
    var isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    var observationsByUnit: String? = null
) {
    companion object {
        fun fromMissionNavEntity(entity: MissionNavEntity): MissionEntity {
            return MissionEntity(
                idUUID = entity.id,
                missionTypes = listOf(),
                openBy = entity.openBy,
                completedBy = entity.completedBy,
                observationsCacem = null,
                observationsByUnit = entity.observationsByUnit,
                observationsCnsp = null,
                facade = null,
                geom = null,
                startDateTimeUtc = entity.startDateTimeUtc,
                endDateTimeUtc = entity.endDateTimeUtc,
                envActions = listOf(),
                isDeleted = entity.isDeleted,
                isGeometryComputedFromControls = false,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,
                isUnderJdp = false
            )
        }
    }
}

typealias EnvMission = MissionEntity
