package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import java.util.*

data class ControlSecurityInput2(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<InfractionInput2>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
) : BaseControlInput() {
    override fun toEntity(): ControlSecurityEntity {
        return ControlSecurityEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId!!,
            actionControlId = actionId!!,
            amountOfControls = amountOfControls ?: 0,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations,
            hasBeenDone = hasBeenDone
        )
    }
}
