package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import java.util.*

data class ControlNavigation(
    val id: UUID,
    val confirmed: Boolean?,
    val observations: String?,
) {
    fun toControlNavigationEntity(missionId: Int, actionId: UUID): ControlNavigationEntity {
        return ControlNavigationEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionId,
            confirmed = confirmed,
            observations = observations
        )
    }
}
