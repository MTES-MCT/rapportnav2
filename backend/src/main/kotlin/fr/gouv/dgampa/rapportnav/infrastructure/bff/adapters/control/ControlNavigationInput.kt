package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation
import java.util.*

data class ControlNavigationInput(
    val id: UUID?,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val observations: String?
) {
    fun toControlNavigation(): ControlNavigation {
        return ControlNavigation(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            confirmed = confirmed,
            observations = observations
        )
    }
}
