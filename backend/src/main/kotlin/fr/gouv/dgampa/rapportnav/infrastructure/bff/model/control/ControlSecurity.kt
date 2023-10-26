package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import java.util.*

data class ControlSecurity(
    val id: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val observations: String?,
) {
    fun toControlSecurityEntity(missionId: Int, actionId: UUID): ControlSecurityEntity {
        return ControlSecurityEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations
        )
    }
}
