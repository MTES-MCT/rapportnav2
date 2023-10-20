package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import java.time.ZonedDateTime
import java.util.*

data class ControlSecurity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
)
