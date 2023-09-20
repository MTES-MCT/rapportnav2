package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import kotlinx.serialization.Serializable

@Serializable
data class ControlUnit(
    val id: Int,
    val administration: String,
    val isArchived: Boolean,
    val name: String,
    val resources: List<ControlResource>,
    val contact: String? = null,
)
