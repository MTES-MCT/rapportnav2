package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ControlUnit(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("administration")
    val administration: String,

    @JsonProperty("isArchived")
    val isArchived: Boolean,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("resources")
    val resources: List<ControlResource>,

    @JsonProperty("contact")
    val contact: String? = null
)
