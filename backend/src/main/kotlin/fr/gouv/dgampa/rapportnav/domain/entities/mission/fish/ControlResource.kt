package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ControlResource(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("name")
    val name: String
)
