package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurity
import java.util.*

data class ControlSecurityInput(
    val id: UUID?,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val observations: String?
) {
    fun toControlSecurity(): ControlSecurity {
        return ControlSecurity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            confirmed = confirmed,
            observations = observations
        )
    }
}
