package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import kotlinx.serialization.Serializable

@Serializable
data class MultiPolygon(
    val type: String,
    val coordinates: List<List<List<List<Double>>>>,
)
