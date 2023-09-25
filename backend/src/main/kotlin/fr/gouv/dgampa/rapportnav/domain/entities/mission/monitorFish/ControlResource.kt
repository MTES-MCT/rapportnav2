package fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish

import kotlinx.serialization.Serializable

@Serializable
data class ControlResource(
    val id: Int,
    val name: String,
)
