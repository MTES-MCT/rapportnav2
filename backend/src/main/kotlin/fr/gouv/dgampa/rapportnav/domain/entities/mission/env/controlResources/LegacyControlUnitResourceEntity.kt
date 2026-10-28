package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class LegacyControlUnitResourceEntity @JsonCreator constructor(
    @field:JsonProperty("id") val id: Int,
    @field:JsonProperty("controlUnitId") val controlUnitId: Int? = null,
    @field:JsonProperty("name") val name: String? = null,
    @field:JsonProperty("type") val type: ControlUnitResourceType? = null,
    // Per-resource usage values (ULAM). Only one applies, driven by [type] via usageKind():
    // nbKms for CAR/MOTORCYCLE, nbEngineHours for boats. Stored in our DB (MonitorEnv can't hold them),
    // stitched back onto the resource by resourceId on read. Omitted from JSON when null so they are not
    // leaked to MonitorEnv on the mission patch (see MissionEnvInput.toPatchMissionInput, which also strips them).
    @field:JsonProperty("nbKms") @field:JsonInclude(JsonInclude.Include.NON_NULL) val nbKms: Double? = null,
    @field:JsonProperty("nbEngineHours") @field:JsonInclude(JsonInclude.Include.NON_NULL) val nbEngineHours: Double? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LegacyControlUnitResourceEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
