package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LegacyControlUnitResourceEntity @JsonCreator constructor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("controlUnitId") val controlUnitId: Int? = null,
    @JsonProperty("name") val name: String? = null,
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
