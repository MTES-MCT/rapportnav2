package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class PatchMissionInput @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @JsonProperty("missionTypes") val missionTypes: List<MissionTypeEnum>? = null,
    @JsonProperty("controlUnits") var controlUnits: List<LegacyControlUnitEntity>? = listOf(),
    @JsonProperty("startDateTimeUtc") val startDateTimeUtc: Instant? = null,
    @JsonProperty("endDateTimeUtc") val endDateTimeUtc: Instant? = null,
    @JsonProperty("isUnderJdp") val isUnderJdp: Boolean? = null,
    @JsonProperty("observationsByUnit") val observationsByUnit: String? = null
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
