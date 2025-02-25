package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

data class MissionEntity(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>,
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
    val observationsByUnit: String? = null
) {

    companion object {
        fun fromMissionEnvEntity(missionEnvEntity: MissionEnvEntity): MissionEntity{
            return MissionEntity(
                id = missionEnvEntity.id,
                startDateTimeUtc = missionEnvEntity.startDateTimeUtc,
                endDateTimeUtc = missionEnvEntity.endDateTimeUtc,
                missionTypes = missionEnvEntity.missionTypes,
                missionSource = missionEnvEntity.missionSource ?: MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = missionEnvEntity.hasMissionOrder ?: false,
                isGeometryComputedFromControls = missionEnvEntity.isGeometryComputedFromControls ?: false,
                isUnderJdp = missionEnvEntity.isUnderJdp ?: false,
                isDeleted = missionEnvEntity.isDeleted ?: false,
                controlUnits = missionEnvEntity.controlUnits ?: listOf(),
                observationsByUnit = missionEnvEntity.observationsByUnit,
                openBy = missionEnvEntity.openBy,
                completedBy = missionEnvEntity.completedBy,
                geom = missionEnvEntity.geom,
                facade = missionEnvEntity.facade,
                observationsCnsp = missionEnvEntity.observationsCnsp,
                envActions = missionEnvEntity.envActions,
                observationsCacem = missionEnvEntity.observationsCacem
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MissionEntity

        if (id != other.id) return false
        if (missionTypes != other.missionTypes) return false
        if (startDateTimeUtc != other.startDateTimeUtc) return false
        if (endDateTimeUtc != other.endDateTimeUtc) return false
        if (observationsByUnit != other.observationsByUnit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + missionTypes.hashCode()
        result = 31 * result + startDateTimeUtc.hashCode()
        result = 31 * result + (endDateTimeUtc?.hashCode() ?: 0)
        result = 31 * result + (observationsByUnit?.hashCode() ?: 0)
        return result
    }
}

typealias EnvMission = MissionEntity
