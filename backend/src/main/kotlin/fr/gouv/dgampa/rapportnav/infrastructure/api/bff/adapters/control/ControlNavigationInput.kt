package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import java.util.*

data class ControlNavigationInput(
    val id: UUID?,
    val missionId: String,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val observations: String?,
) {
    fun toControlNavigation(): ControlNavigationEntity {
        return ControlNavigationEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations,
        )
    }
}
