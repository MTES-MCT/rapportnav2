package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import java.time.Instant

data class MissionEnvInput(
    val missionId: Int,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf()
) {
    companion object {
        fun fromMissionEnvEntity(missionEnvEntity: MissionEnvEntity): MissionEnvInput {
            return MissionEnvInput(
                missionId = missionEnvEntity.id!!,
                startDateTimeUtc = missionEnvEntity.startDateTimeUtc,
                endDateTimeUtc = missionEnvEntity.endDateTimeUtc,
                missionTypes = missionEnvEntity.missionTypes,
                observationsByUnit = missionEnvEntity.observationsByUnit,
            )
        }

        fun fromMissionEntity(missionEntity: MissionEntity): MissionEnvInput {
            return MissionEnvInput(
                missionId = missionEntity.id!!,
                startDateTimeUtc = missionEntity.startDateTimeUtc,
                endDateTimeUtc = missionEntity.endDateTimeUtc,
                missionTypes = missionEntity.missionTypes,
                observationsByUnit = missionEntity.observationsByUnit,
            )
        }
    }

    fun toMissionEnvEntity(missionFromDb: MissionEntity): MissionEnvEntity {
        return MissionEnvEntity(
            id = missionFromDb.id,
            missionSource = missionFromDb.missionSource,
            envActions = missionFromDb.envActions,
            observationsCacem = missionFromDb.observationsCacem,
            observationsCnsp = missionFromDb.observationsCnsp,
            facade = missionFromDb.facade,
            geom = missionFromDb.geom,
            completedBy = missionFromDb.completedBy,
            openBy = missionFromDb.openBy,
            isDeleted = missionFromDb.isDeleted,
            isUnderJdp = missionFromDb.isUnderJdp,
            isGeometryComputedFromControls = missionFromDb.isGeometryComputedFromControls,
            hasMissionOrder = missionFromDb.hasMissionOrder,
            controlUnits = missionFromDb.controlUnits,
            startDateTimeUtc = startDateTimeUtc ?: missionFromDb.startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc ?: missionFromDb.endDateTimeUtc,
            missionTypes = missionTypes ?: missionFromDb.missionTypes,
            observationsByUnit = observationsByUnit ?: missionFromDb.observationsByUnit,
        )
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MissionEnvInput

        if (missionId != other.missionId) return false
        if (missionTypes != other.missionTypes) return false
        if (startDateTimeUtc != other.startDateTimeUtc) return false
        if (endDateTimeUtc != other.endDateTimeUtc) return false
        if (observationsByUnit != other.observationsByUnit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = missionId.hashCode()
        result = 31 * result + missionTypes.hashCode()
        result = 31 * result + startDateTimeUtc.hashCode()
        result = 31 * result + (endDateTimeUtc?.hashCode() ?: 0)
        result = 31 * result + (observationsByUnit?.hashCode() ?: 0)
        return result
    }
}

