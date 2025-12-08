package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LegacyControlUnitResourceEntity @JsonCreator constructor(
    @field:JsonProperty("id") val id: Int,
    @field:JsonProperty("controlUnitId") val controlUnitId: Int? = null,
    @field:JsonProperty("name") val name: String? = null,
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
