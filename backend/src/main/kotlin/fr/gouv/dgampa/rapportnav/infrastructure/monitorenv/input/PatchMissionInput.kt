package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import java.time.Instant

data class PatchMissionInput(
    val isUnderJdp: Boolean? = null,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionTypes: List<MissionTypeEnum>? =  null,
    var controlUnits: List<LegacyControlUnitEntity>? = null
) {

    fun withControlUnits(
        resources: List<LegacyControlUnitResourceEntity>?,
        controlUnits: List<LegacyControlUnitEntity>
    ): PatchMissionInput {
        val controlUnitId = resources?.firstOrNull()?.controlUnitId
        this.controlUnits = controlUnits.map { controlUnit ->
            controlUnit.takeIf { it.id != controlUnitId } ?: controlUnit.copy(
                resources = resources?.toMutableList() ?: controlUnit.resources?.toMutableList()
            )
        }
        return this
    }

    fun hasNotChanged(other: MissionEntity?): Boolean {
        if (other == null) return false
        if (isUnderJdp != other.isUnderJdp) return false
        if (missionTypes != other.missionTypes) return false
        if (startDateTimeUtc != other.startDateTimeUtc) return false
        if (endDateTimeUtc != other.endDateTimeUtc) return false
        if (observationsByUnit != other.observationsByUnit) return false
        if (controlUnits?.size != other.controlUnits.size) return false
        if (controlUnits?.toSet() != other.controlUnits.toSet()) return false
        return true
    }
}
