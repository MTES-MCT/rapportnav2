package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import java.util.*

data class ControlNavigation(
    val id: UUID,
    val confirmed: Boolean?,
    val observations: String?,
)
