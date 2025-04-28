package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

data class MissionEntity(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf(),
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    var envActions: List<EnvActionEntity>? = listOf(),
    val isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    val observationsByUnit: String? = null,
    val controlUnitIdOwner: Int? = null,
    val missionIdString: String? = null
) {
    companion object {
        fun fromMissionNavModel(model: MissionModel): MissionEntity {
            return MissionEntity(
                id = model.id,
                missionTypes = listOf(),
                controlUnits = model.controlUnits.map { LegacyControlUnitEntity(
                    id = it
                ) },
                openBy = model.openBy,
                completedBy = model.completedBy,
                observationsCacem = null,
                observationsByUnit = model.observationsByUnit,
                observationsCnsp = null,
                facade = null,
                geom = null,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                envActions = listOf(),
                isDeleted = model.isDeleted,
                isGeometryComputedFromControls = false,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,
                isUnderJdp = false,
                controlUnitIdOwner = model.controlUnitIdOwner,
                missionIdString = model.missionIdString.toString()
            )
        }
    }
}

typealias EnvMission = MissionEntity
