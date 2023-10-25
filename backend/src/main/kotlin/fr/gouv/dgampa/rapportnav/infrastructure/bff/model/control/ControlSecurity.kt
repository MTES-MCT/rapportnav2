package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import java.util.*

data class ControlSecurity(
    val id: UUID,
    val confirmed: Boolean?,
    val observations: String?,
) {
    fun toControlSecurityEntity(missionId: Int, actionId: UUID): ControlSecurityEntity {
        return ControlSecurityEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionId,
            confirmed = confirmed,
            observations = observations
        )
    }
}
