package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import java.util.*

data class ControlSecurity(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<Infraction>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
) : BaseControl() {
    override fun toEntity(): ControlSecurityEntity {
        return ControlSecurityEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId!!,
            actionControlId = actionId!!,
            amountOfControls = amountOfControls ?: 0,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations,
            hasBeenDone = hasBeenDone,
            infractions = infractions?.map { it.toInfractionEntity() }
        )
    }

    companion object {
        fun fromControlSecurityEntity(entity: ControlSecurityEntity?): ControlSecurity {
            return ControlSecurity(
                id = entity?.id,
                amountOfControls = entity?.amountOfControls,
                unitShouldConfirm = entity?.unitShouldConfirm,
                unitHasConfirmed = entity?.unitHasConfirmed,
                observations = entity?.observations,
                hasBeenDone = entity?.hasBeenDone,
                infractions = entity?.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
