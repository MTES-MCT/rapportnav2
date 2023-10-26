package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import java.time.ZonedDateTime
import java.util.*

data class ControlSecurityEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toControlSecurity(): ControlSecurity {
        return ControlSecurity(
            id = id,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations
        )
    }
}
