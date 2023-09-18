package fr.gouv.dgampa.rapportnav.domain.entities.control

import java.util.*

data class ControlEquipmentAndSecurity(
    val id: UUID,
    val missionId: Int,
    val confirmed: Boolean?,
    val observations: String?
)
