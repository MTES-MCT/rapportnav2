package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import java.util.*

data class ControlSecurity(
    val id: UUID,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val observations: String?,
    val infractions: List<Infraction>? = null
) {
    fun toControlSecurityEntity(missionId: String, actionId: String): ControlSecurityEntity {
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

    companion object {
        fun fromControlSecurityEntity(control: ControlSecurityEntity?) = control?.let {
            ControlSecurity(
                id = it.id,
                amountOfControls = control.amountOfControls,
                unitShouldConfirm = control.unitShouldConfirm,
                unitHasConfirmed = control.unitHasConfirmed,
                observations = control.observations,
                infractions = control.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
