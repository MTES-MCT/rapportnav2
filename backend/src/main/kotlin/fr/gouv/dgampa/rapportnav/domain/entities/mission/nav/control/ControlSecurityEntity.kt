package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import java.time.ZonedDateTime
import java.util.*

data class ControlSecurityEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toControlSecurity(): ControlSecurity {
        return ControlSecurity(
            id = id,
            confirmed = confirmed,
            observations = observations
        )
    }
}
