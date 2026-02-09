package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.Patchable
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant
import java.util.*

data class MissionEnvEntity(
    val id: Int? = null,
    val idUUID: UUID? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf(),
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    @Patchable
    var observationsByUnit: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    @Patchable
    var startDateTimeUtc: Instant,
    @Patchable
    var endDateTimeUtc: Instant? = null,
    val createdAtUtc: Instant? = null,
    val updatedAtUtc: Instant? = null,
    val envActions: List<ActionEnvEntity>? = listOf(),
    val isGeometryComputedFromControls: Boolean? = false,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean? = false,
    val isUnderJdp: Boolean? = false,
    val isDeleted: Boolean? = false
) {
    companion object {
        fun fromMissionNavEntity(entity: MissionNavEntity): MissionEnvEntity {
            return MissionEnvEntity(
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
