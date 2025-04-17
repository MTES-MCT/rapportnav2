package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

data class MissionEnv(
    val id: String? = null,
    val missionTypes: List<MissionTypeEnum>,
    var controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean = false,
    val isGeometryComputedFromControls: Boolean = false,
    val observationsByUnit: String? = null
) {

    companion object {
        fun fromMissionEnvEntity(mission: MissionEnvEntity): MissionEnv {
            return MissionEnv(
                id = mission.id,
                missionSource = mission.missionSource ?: MissionSourceEnum.RAPPORT_NAV,
                startDateTimeUtc = mission.startDateTimeUtc,
                endDateTimeUtc = mission.endDateTimeUtc,
                openBy = mission.openBy,
                controlUnits = mission.controlUnits ?: listOf(),
                hasMissionOrder = mission.hasMissionOrder ?: false,
                missionTypes = mission.missionTypes,
                observationsByUnit = mission.observationsByUnit
            )
        }

        fun fromMissionEntity(mission: MissionEntity): MissionEnv {
            return MissionEnv(
                id = mission.id,
                missionTypes = mission.missionTypes ?: listOf(),
                controlUnits = mission.controlUnits,
                openBy = mission.openBy,
                completedBy = mission.completedBy,
                observationsCacem = mission.observationsCacem,
                observationsCnsp = mission.observationsCnsp,
                facade = mission.facade,
                geom = mission.geom,
                startDateTimeUtc = mission.startDateTimeUtc,
                endDateTimeUtc = mission.endDateTimeUtc,
                missionSource = mission.missionSource,
                hasMissionOrder = mission.hasMissionOrder,
                isUnderJdp = mission.isUnderJdp,
                isGeometryComputedFromControls = mission.isGeometryComputedFromControls,
                observationsByUnit = mission.observationsByUnit
            )
        }
    }
}
