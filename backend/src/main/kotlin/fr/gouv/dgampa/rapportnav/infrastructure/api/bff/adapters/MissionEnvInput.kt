package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import java.time.Instant

data class MissionEnvInput(
    val missionId: Int,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf(),
    val resources: List<LegacyControlUnitResourceEntity>? = listOf(),
    val isUnderJdp: Boolean? = null,
) {
    companion object {

        fun fromMissionEntity(missionEntity: MissionEntity, controlUnitId: Int? = null): MissionEnvInput {
            return MissionEnvInput(
                missionId = missionEntity.id!!,
                startDateTimeUtc = missionEntity.startDateTimeUtc,
                endDateTimeUtc = missionEntity.endDateTimeUtc,
                missionTypes = missionEntity.missionTypes,
                isUnderJdp = missionEntity.isUnderJdp,
                observationsByUnit = missionEntity.observationsByUnit,
                resources = missionEntity.controlUnits.filter { it.id == controlUnitId }
                    .flatMap { it.resources!! }
            )
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MissionEnvInput

        if (missionId != other.missionId) return false
        if(isUnderJdp != other.isUnderJdp) return false
        if (missionTypes != other.missionTypes) return false
        if (startDateTimeUtc != other.startDateTimeUtc) return false
        if (endDateTimeUtc != other.endDateTimeUtc) return false
        if (observationsByUnit != other.observationsByUnit) return false
        if (resources?.size != other.resources?.size) return false
        if (resources?.toSet() != other.resources?.toSet()) return false
        return true
    }

    override fun hashCode(): Int {
        var result = missionId.hashCode()
        result = 31 * result + missionTypes.hashCode()
        result = 31 * result + startDateTimeUtc.hashCode()
        result = 31 * result + (endDateTimeUtc?.hashCode() ?: 0)
        result = 31 * result + (observationsByUnit?.hashCode() ?: 0)
        result = 31 * result + (resources?.toSet()?.hashCode() ?: 0)
        return result
    }
}

