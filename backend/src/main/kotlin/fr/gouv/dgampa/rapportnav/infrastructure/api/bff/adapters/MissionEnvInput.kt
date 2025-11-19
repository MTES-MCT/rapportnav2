package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
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

    fun toPatchMissionInput(controlUnits: List<LegacyControlUnitEntity>): PatchMissionInput {
        val controlUnitId = resources?.firstOrNull()?.controlUnitId
        return PatchMissionInput(
            isUnderJdp = isUnderJdp,
            missionTypes = missionTypes,
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc,
            observationsByUnit = observationsByUnit
            //TODO HOT FIX, no patch of control units before fixing the PROD issue
            /*controlUnits = controlUnits.map { controlUnit ->
                controlUnit.takeIf { it.id != controlUnitId } ?: controlUnit.copy(
                    resources = resources?.toMutableList() ?: controlUnit.resources?.toMutableList()
                )
            }*/
        )
    }

    companion object {

        fun fromMissionEntity(entity: MissionEntity, controlUnitId: Int? = null): MissionEnvInput {
            return MissionEnvInput(
                missionId = entity.id!!,
                isUnderJdp = entity.isUnderJdp,
                missionTypes = entity.missionTypes,
                endDateTimeUtc = entity.endDateTimeUtc,
                startDateTimeUtc = entity.startDateTimeUtc,
                observationsByUnit = entity.observationsByUnit,
                resources = entity.controlUnits.filter { it.id == controlUnitId }
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

