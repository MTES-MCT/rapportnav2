package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MultiPolygon(
    @JsonProperty("type")
    val type: String,

    @JsonProperty("coordinates")
    val coordinates: List<List<List<List<Double>>>>
)
