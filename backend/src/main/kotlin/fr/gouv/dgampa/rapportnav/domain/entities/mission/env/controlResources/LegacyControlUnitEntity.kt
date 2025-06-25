package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LegacyControlUnitEntity @JsonCreator constructor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("administration") val administration: String? = null,
    @JsonProperty("isArchived") val isArchived: Boolean? = false,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("resources") val resources: MutableList<LegacyControlUnitResourceEntity>? = mutableListOf(),
    @JsonProperty("contact") val contact: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LegacyControlUnitEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
